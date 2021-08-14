package com.github.virusbear.metrix

import com.github.virusbear.metrix.MetrixEvents.BindMeters
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.MinecraftServer

object MetrixEvents {
    @JvmField
    val BIND_METERS = EventFactory.createArrayBacked(BindMeters::class.java) { listeners ->
        BindMeters { registry, server ->
            listeners.forEach {
                it.onBindMeter(registry, server)
            }
        }
    }

    @FunctionalInterface
    fun interface BindMeters {
        fun onBindMeter(registry: MeterRegistry, server: MinecraftServer)
    }
}