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

import com.virusbear.metrix.Counter
import com.virusbear.metrix.Tags
import com.virusbear.metrix.micrometer.toTags
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Counter as MicrometerCounter
import io.micrometer.core.instrument.Tags as MicrometerTags

class MetrixCounter internal constructor(
    override val name: String,
    override val tags: Tags,
    private val registry: MeterRegistry
): Counter {
    private val counter = HashMap<Tags, MetrixTaggedCounter>()

    override fun get(tags: Tags): Counter.TaggedCounter =
        counter.computeIfAbsent(tags, ::registerCounter)

    override fun minusAssign(tags: Tags) {
        counter.remove(tags)?.let {
            it.close()
            registry.remove(it.counter)
        }
    }

    override fun tags(): List<Tags> =
        counter.keys.toList()

    private fun registerCounter(tags: Tags) =
        MetrixTaggedCounter(
            MicrometerCounter
                .builder(name)
                .tags(MicrometerTags.of(tags.toTags()))
                .tags(this.tags.toTags())
                .register(registry)
        )

}