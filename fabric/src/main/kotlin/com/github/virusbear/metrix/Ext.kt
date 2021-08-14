package com.github.virusbear.metrix

import net.minecraft.world.World

internal fun metrixIdentifier(name: String): Identifier = Identifier("metrix", name)

val World.name: String
    get() =
        this.dimension.skyProperties.path