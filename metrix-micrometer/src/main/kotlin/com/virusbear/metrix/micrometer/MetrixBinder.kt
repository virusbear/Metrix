package com.virusbear.metrix.micrometer

import com.virusbear.metrix.*
import com.virusbear.metrix.micrometer.meter.MetrixCounter
import com.virusbear.metrix.micrometer.meter.MetrixGauge
import com.virusbear.metrix.micrometer.meter.MetrixTimer
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import com.virusbear.metrix.MeterRegistry as MetrixRegistry

class MetrixBinder: MeterBinder, MetrixRegistry {
    private lateinit var registry: MeterRegistry

    override fun bindTo(registry: MeterRegistry) {
        this.registry = registry
    }

    override fun gauge(id: Identifier, tags: Tags): Gauge =
        registerMeter(id.toMicrometer(), tags, ::MetrixGauge)

    override fun timer(id: Identifier, tags: Tags): Timer =
        registerMeter(id.toMicrometer(), tags) { meterName, tags, registry ->
            MetrixTimer(meterName, tags, registry)
        }

    override fun counter(id: Identifier, tags: Tags): Counter =
        registerMeter(id.toMicrometer(), tags, ::MetrixCounter)

    private fun <T: Meter> registerMeter(name: String, tags: Tags, ctor: (String, Tags, MeterRegistry) -> T): T {
        requireBound()

        return ctor(name, tags, registry)
    }

    private fun requireBound() {
        check(this::registry.isInitialized) {
            "MetrixBinder not bound to Micrometer MeterRegistry. Unable to create Meters"
        }
    }
}