/*
 * Copyright 2023 Virusbear
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.virusbear.metrix.micrometer.meter

import com.virusbear.metrix.Gauge
import com.virusbear.metrix.Tags
import com.virusbear.metrix.micrometer.toTags
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Gauge as MicrometerGauge

class MetrixGauge internal constructor(
    override val name: String,
    override val tags: Tags,
    private val registry: MeterRegistry
): Gauge {
    private val gauges = HashMap<Tags, GaugeSupplier>()

    override fun get(tags: Tags): Gauge.TaggedGauge {
        val supplier = gauges.computeIfAbsent(tags, ::createManualGauge)

        require(supplier is ManualGaugeSupplier) { "Automatic gauge not supported by TaggedGauge." }

        return supplier
    }

    override fun register(tags: Tags, supplier: () -> Double) {
        require(tags !in gauges) { "Registering automatic supplier for existing gauge not allowed" }

        gauges[tags] = createAutomaticGauge(tags, supplier)
    }

    override fun minusAssign(tags: Tags) {
        gauges.remove(tags)?.close()
    }

    override fun tags(): List<Tags> =
        gauges.keys.toList()

    private fun createManualGauge(tags: Tags): GaugeSupplier =
        createGauge(tags, ManualGaugeSupplier(registry))

    private fun createAutomaticGauge(tags: Tags, onSupply: () -> Double): GaugeSupplier =
        createGauge(tags, AutomaticGaugeSupplier(registry, onSupply))

    private fun createGauge(tags: Tags, supplier: GaugeSupplier): GaugeSupplier =
        supplier.apply {
            gauge = MicrometerGauge
                .builder(name, supplier::supply)
                .tags(this@MetrixGauge.tags.toTags())
                .tags(tags.toTags())
                .register(registry)
        }
}