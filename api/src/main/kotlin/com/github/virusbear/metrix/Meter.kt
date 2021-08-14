package com.github.virusbear.metrix

interface Meter {
    val name: String
    val tags: Tags

    operator fun minusAssign(tags: Tags)
    fun tags(): List<Tags>
}