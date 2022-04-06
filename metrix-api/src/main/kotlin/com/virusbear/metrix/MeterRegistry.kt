package com.virusbear.metrix

interface MeterRegistry {
    fun gauge(id: Identifier, tags: Tags = emptyMap()): Gauge
    fun timer(id: Identifier, tags: Tags = emptyMap()): Timer
    fun counter(id: Identifier, tags: Tags = emptyMap()): Counter
}