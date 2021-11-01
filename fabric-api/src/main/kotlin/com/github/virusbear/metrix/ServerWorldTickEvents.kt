package com.github.virusbear.metrix

import com.github.virusbear.metrix.ServerWorldTickEvents.EndTick
import com.github.virusbear.metrix.ServerWorldTickEvents.StartTick
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.world.ServerWorld

object ServerWorldTickEvents {
    @JvmStatic
    val START_SERVER_WORLD_TICK = EventFactory.createArrayBacked(StartTick::class.java) { listeners ->
        StartTick { world ->
            listeners.forEach {
                it.onStartTick(world)
            }
        }
    }

    @JvmStatic
    val END_SERVER_WORLD_TICK = EventFactory.createArrayBacked(EndTick::class.java) { listeners ->
        EndTick { world ->
            listeners.forEach {
                it.onEndTick(world)
            }
        }
    }

    @FunctionalInterface
    fun interface StartTick {
        fun onStartTick(world: ServerWorld)
    }

    @FunctionalInterface
    fun interface EndTick {
        fun onEndTick(SERVER: ServerWorld)
    }
}