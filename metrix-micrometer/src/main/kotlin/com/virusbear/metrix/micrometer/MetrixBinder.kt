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
        registerMeter(id.toMicrometer(), tags, ::MetrixTimer)

    override fun counter(id: Identifier, tags: Tags): Counter =
        registerMeter(id.toMicrometer(), tags, ::MetrixCounter)

    private fun <T: Meter<*>> registerMeter(name: String, tags: Tags, ctor: (String, Tags, MeterRegistry) -> T): T {
        requireBound()

        return ctor(name, tags, registry)
    }

    private fun requireBound() {
        check(this::registry.isInitialized) {
            "MetrixBinder not bound to Micrometer MeterRegistry. Unable to create Meters"
        }
    }
}