package com.virusbear.metrix

interface MeterRegistry {
    fun gauge(id: Identifier): Gauge
    fun timer(id: Identifier): Timer
    fun counter(id: Identifier): Counter
}