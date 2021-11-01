package com.github.virusbear.metrix

import net.minecraft.world.World

val World.name: String
    get() =
        this.dimension.skyProperties.path