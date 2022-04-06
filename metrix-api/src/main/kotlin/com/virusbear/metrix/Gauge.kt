package com.virusbear.metrix

interface Gauge: Meter {
    operator fun get(tags: Tags = emptyMap()): TaggedGauge

    operator fun set(tags: Tags = emptyMap(), value: Double)
    fun register(tags: Tags = emptyMap(), supplier: () -> Double)

    interface TaggedGauge {
        operator fun inc(): TaggedGauge
        operator fun dec(): TaggedGauge

        operator fun plusAssign(delta: Double)
        operator fun plusAssign(delta: Int) {
            this += delta.toDouble()
        }
        operator fun plusAssign(delta: Long) {
            this += delta.toDouble()
        }

        operator fun minusAssign(delta: Double)
        operator fun minusAssign(delta: Int) {
            this -= delta.toDouble()
        }
        operator fun minusAssign(delta: Long) {
            this -= delta.toDouble()
        }

        val value: Double
    }
}