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

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import java.io.Closeable
import java.util.concurrent.TimeUnit
import com.virusbear.metrix.Timer.TaggedTimer
import com.virusbear.metrix.Timer.Sample
import kotlin.time.Duration

class MetrixTaggedTimer internal constructor(
    private val timer: Timer,
    private val registry: MeterRegistry
): TaggedTimer, Closeable {
    private var active = true

    override fun sample(): Sample {
        require(active) { "Unable to sample closed timer." }

        return MetrixTimerSample(timer, Timer.start(registry))
    }

    override fun record(duration: Duration) {
        if(active) {
            timer.record(duration.inWholeNanoseconds, TimeUnit.NANOSECONDS)
        }
    }

    override fun close() {
        registry.remove(timer)
        active = false
    }
}