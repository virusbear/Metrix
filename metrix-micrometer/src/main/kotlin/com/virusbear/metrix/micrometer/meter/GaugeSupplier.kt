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

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry

internal sealed class GaugeSupplier(
    private val registry: MeterRegistry
): AutoCloseable {

    abstract fun supply(): Double

    internal lateinit var gauge: Gauge

    override fun close() {
        registry.remove(gauge)
    }
}