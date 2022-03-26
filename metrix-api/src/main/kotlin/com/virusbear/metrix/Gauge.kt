package com.virusbear.metrix

interface Gauge: Meter {
    operator fun set(tags: Tags = emptyMap(), value: Double)
    fun register(tags: Tags = emptyMap(), supplier: () -> Double)
}