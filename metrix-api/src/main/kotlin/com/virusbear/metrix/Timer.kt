package com.virusbear.metrix

import java.time.Duration

interface Timer: Meter {
    fun sample(tags: Tags = emptyMap()): Sample
    fun record(sample: Sample, tags: Tags = emptyMap())
    fun record(duration: Duration, tags: Tags = emptyMap())
    fun start(tags: Tags = emptyMap())
    fun stop(tags: Tags = emptyMap())

    interface Sample
}