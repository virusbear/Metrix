package com.virusbear.metrix.micrometer.meter

import com.virusbear.metrix.Tags
import com.virusbear.metrix.Timer
import com.virusbear.metrix.micrometer.toTags
import io.micrometer.core.instrument.MeterRegistry
import java.time.Duration
import io.micrometer.core.instrument.Timer as MicrometerTimer
import io.micrometer.core.instrument.Timer.Sample as MicrometerTimerSample

class MetrixTimer(override val name: String, override val tags: Tags, private val registry: MeterRegistry): Timer {
    private val timers = HashMap<Tags, MicrometerTimer>()

    private val samples = HashMap<Tags, Timer.Sample>()

    override fun sample(tags: Tags): Timer.Sample =
        MetrixTimerSample(MicrometerTimer.start(registry))

    override fun record(sample: Timer.Sample, tags: Tags) {
        require(sample is MetrixTimerSample) { "${MetrixTimer::class.simpleName} can only record samples of type ${MetrixTimerSample::class.simpleName}" }

        sample.sample.stop(this[tags])
    }

    override fun record(duration: Duration, tags: Tags) {
        this[tags].record(duration)
    }

    override fun start(tags: Tags) {
        if(tags !in samples) {
            samples[tags] = sample(tags)
        }
    }

    override fun stop(tags: Tags) {
        samples[tags]?.let {
            record(samples[tags]!!, tags)
            samples -= tags
        }
    }

    override fun minusAssign(tags: Tags) {
        samples -= tags

        timers.remove(tags)?.let {
            registry.remove(it)
        }
    }

    override fun tags(): List<Tags> =
        timers.keys.toList()

    private operator fun get(tags: Tags): MicrometerTimer =
        timers.computeIfAbsent(tags, ::createTimer)

    private fun createTimer(tags: Tags): MicrometerTimer =
        MicrometerTimer
            .builder(name).tags(this.tags.toTags())
            .tags(tags.toTags())
            .register(registry)

    data class MetrixTimerSample(internal val sample: MicrometerTimerSample): Timer.Sample
}