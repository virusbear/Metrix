package com.virusbear.metrix.micrometer.meter

import com.virusbear.metrix.Counter
import com.virusbear.metrix.Tags
import com.virusbear.metrix.micrometer.toTags
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Counter as MicrometerCounter
import io.micrometer.core.instrument.Tags as MicrometerTags

class MetrixCounter(override val name: String, override val tags: Tags, private val registry: MeterRegistry): Counter {
    private val counter = HashMap<Tags, MetrixTaggedCounter>()

    override fun get(tags: Tags): Counter.TaggedCounter =
        counter.computeIfAbsent(tags, ::registerCounter)

    override fun minusAssign(tags: Tags) {
        counter.remove(tags)?.let {
            it.active = false
            registry.remove(it.counter)
        }
    }

    override fun tags(): List<Tags> =
        counter.keys.toList()

    private fun registerCounter(tags: Tags) = MetrixTaggedCounter(
        MicrometerCounter
            .builder(name)
            .tags(MicrometerTags.of(tags.toTags()))
            .tags(this.tags.toTags())
            .register(registry)
    )

    inner class MetrixTaggedCounter(internal val counter: MicrometerCounter): Counter.TaggedCounter {
        internal var active = true

        override fun inc(): Counter.TaggedCounter {
            if(active) {
                counter.increment()
            }

            return this
        }

        override fun plusAssign(amount: Long) {
            if(active) {
                counter.increment(amount.toDouble())
            }
        }
    }
}