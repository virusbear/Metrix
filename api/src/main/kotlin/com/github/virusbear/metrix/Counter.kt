package com.github.virusbear.metrix

interface Counter: Meter {
    operator fun get(tags: Tags = emptyMap()): TaggedCounter

    interface TaggedCounter {
        operator fun inc(): TaggedCounter
        operator fun plusAssign(amount: Long)
    }
}