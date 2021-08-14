package com.github.virusbear.metrix

import java.time.Duration

interface Timer: Meter {
    fun sample(tags: Tags = emptyMap()): Sample
    fun record(tags: Tags = emptyMap(), sample: Sample)
    fun record(tags: Tags = emptyMap(), duration: Duration)
    fun start(tags: Tags = emptyMap())
    fun stop(tags: Tags = emptyMap())

    interface Sample
}