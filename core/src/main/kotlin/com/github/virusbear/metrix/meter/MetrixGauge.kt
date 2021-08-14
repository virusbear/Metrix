package com.github.virusbear.metrix.meter

import com.github.virusbear.metrix.Gauge
import com.github.virusbear.metrix.Tags
import com.github.virusbear.metrix.toTags
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Gauge as MicrometerGauge

class MetrixGauge(override val name: String, override val tags: Tags, private val registry: MeterRegistry): Gauge {
    private val gauges = HashMap<Tags, Pair<GaugeSupplier, MicrometerGauge>>()

    override fun set(tags: Tags, value: Double) {
        val (supplier, _) = gauges.computeIfAbsent(tags, ::createManualGauge)

        require(supplier is GaugeSupplier.Manual) { "Setting value for automatic gauge not allowed" }

        supplier.set(value)
    }

    override fun register(tags: Tags, supplier: () -> Double) {
        require(tags !in gauges) { "Registering automatic supplier for existing gauge not allowed" }

        gauges[tags] = createAutomaticGauge(tags, supplier)
    }

    override fun minusAssign(tags: Tags) {
        gauges.remove(tags)?.let { (_, meter) ->
            registry.remove(meter)
        }
    }

    override fun tags(): List<Tags> =
        gauges.keys.toList()

    private fun createManualGauge(tags: Tags): Pair<GaugeSupplier, MicrometerGauge> =
        createGauge(tags, GaugeSupplier.Manual())

    private fun createAutomaticGauge(tags: Tags, onSupply: () -> Double): Pair<GaugeSupplier, MicrometerGauge> =
        createGauge(tags, GaugeSupplier.Automatic(onSupply))

    private fun createGauge(tags: Tags, supplier: GaugeSupplier): Pair<GaugeSupplier, MicrometerGauge> =
        supplier to MicrometerGauge
            .builder(name, supplier::supply)
            .tags(this.tags.toTags())
            .tags(tags.toTags())
            .register(registry)

    private sealed class GaugeSupplier {
        class Manual: GaugeSupplier() {
            private var value = 0.0

            fun set(value: Double) {
                synchronized(this.value) {
                    this.value = value
                }
            }

            override fun supply(): Double =
                synchronized(value) {
                    value
                }

        }

        class Automatic(private val supplier: () -> Double): GaugeSupplier() {
            override fun supply(): Double =
                supplier()
        }

        abstract fun supply(): Double
    }
}