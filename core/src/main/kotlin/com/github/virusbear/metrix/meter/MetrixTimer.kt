package com.github.virusbear.metrix.meter

import com.github.virusbear.metrix.Tags
import com.github.virusbear.metrix.Timer
import com.github.virusbear.metrix.toTags
import io.micrometer.core.instrument.MeterRegistry
import org.apache.logging.log4j.LogManager
import java.time.Duration
import io.micrometer.core.instrument.Timer as MicrometerTimer
import io.micrometer.core.instrument.Timer.Sample as MicrometerTimerSample

class MetrixTimer(override val name: String, override val tags: Tags, private val registry: MeterRegistry): Timer {
    companion object {
        val LOG = LogManager.getLogger()
    }

    private val timers = HashMap<Tags, MicrometerTimer>()

    private val samples = HashMap<Tags, Timer.Sample>()

    override fun sample(tags: Tags): Timer.Sample =
        MetrixTimerSample(MicrometerTimer.start(registry))

    override fun record(tags: Tags, sample: Timer.Sample) {
        require(sample is MetrixTimerSample) { "${MetrixTimer::class.simpleName} can only record samples of type ${MetrixTimerSample::class.simpleName}" }

        sample.sample.stop(this[tags])
    }

    override fun record(tags: Tags, duration: Duration) {
        this[tags].record(duration)
    }

    override fun start(tags: Tags) {
        if(tags in samples) {
            LOG.warn("Recording already started for tags $tags. Skipping")
        } else {
            samples[tags] = sample(tags)
        }
    }

    override fun stop(tags: Tags) {
        samples[tags]?.let {
            record(tags, samples[tags]!!)
            samples -= tags
        } ?: LOG.warn("No recording started for tags $tags. Skipping")
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