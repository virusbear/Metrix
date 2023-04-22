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

import com.virusbear.metrix.Tags
import com.virusbear.metrix.Timer
import com.virusbear.metrix.micrometer.toTags
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer as MicrometerTimer

class MetrixTimer internal constructor(override val name: String, override val tags: Tags, private val registry: MeterRegistry): Timer {
    private val timers = HashMap<Tags, MetrixTaggedTimer>()

    override fun get(tags: Tags): Timer.TaggedTimer =
        MetrixTaggedTimer(
            createTimer(tags),
            registry
        )

    override fun minusAssign(tags: Tags) {
        timers.remove(tags)?.close()
    }

    override fun tags(): List<Tags> =
        timers.keys.toList()

    private fun createTimer(tags: Tags): MicrometerTimer =
        MicrometerTimer
            .builder(name).tags(this.tags.toTags())
            .tags(tags.toTags())
            .register(registry)
}