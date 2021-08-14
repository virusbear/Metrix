package com.github.virusbear.metrix

import com.github.virusbear.metrix.meter.MetrixCounter
import com.github.virusbear.metrix.meter.MetrixGauge
import com.github.virusbear.metrix.meter.MetrixTimer
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import com.github.virusbear.metrix.MeterRegistry as MetrixRegistry

class MetrixBinder(private val onBindToRegistry: MetrixBinder.() -> Unit): MeterBinder, MetrixRegistry {
    private lateinit var registry: MeterRegistry

    override fun bindTo(registry: MeterRegistry) {
        this.registry = registry
        onBindToRegistry()
    }

    override fun gauge(id: Identifier): Gauge =
        registerMeter(id.toMicrometer(), ::MetrixGauge)

    override fun timer(id: Identifier): Timer =
        registerMeter(id.toMicrometer()) { meterName, tags, registry ->
            MetrixTimer(meterName, tags, registry)
        }

    override fun counter(id: Identifier): Counter =
        registerMeter(id.toMicrometer(), ::MetrixCounter)

    private fun <T: Meter> registerMeter(name: String, ctor: (String, Tags, MeterRegistry) -> T): T {
        requireBound()

        return ctor(name, mapOf(), registry)
    }

    private fun requireBound() {
        check(this::registry.isInitialized) {
            "MetrixBinder not bound to Micrometer MeterRegistry. Unable to create Meters"
        }
    }
}