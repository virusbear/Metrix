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

import com.virusbear.metrix.Gauge.TaggedGauge
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Gauge

internal class ManualGaugeSupplier(
    registry: MeterRegistry
): GaugeSupplier(registry), TaggedGauge {
    private var active = true
    override var value = 0.0
        private set(value) {
            if(active) {
                field = value
            }
        }

    override operator fun inc(): ManualGaugeSupplier {
        value++
        return this
    }

    override operator fun dec(): ManualGaugeSupplier {
        value--
        return this
    }

    override fun plusAssign(delta: Number) {
        value += delta.toDouble()
    }

    override fun minusAssign(delta: Number) {
        value -= delta.toDouble()
    }

    override fun set(value: Double) {
        this.value = value
    }

    override fun supply(): Double =
        value

    override fun close() {
        super.close()
        active = false
    }
}