package com.github.virusbear.metrix

import com.github.virusbear.metrix.PlayerTickEvents.EndTick
import com.github.virusbear.metrix.PlayerTickEvents.StartTick
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity

object PlayerTickEvents {
    @JvmStatic
    val START_PLAYER_TICK = EventFactory.createArrayBacked(StartTick::class.java) { listeners ->
        StartTick { player ->
            listeners.forEach {
                it.onStartTick(player)
            }
        }
    }

    @JvmStatic
    val END_PLAYER_TICK = EventFactory.createArrayBacked(EndTick::class.java) { listeners ->
        EndTick { player ->
            listeners.forEach {
                it.onEndTick(player)
            }
        }
    }

    @FunctionalInterface
    fun interface StartTick {
        fun onStartTick(player: PlayerEntity)
    }

    @FunctionalInterface
    fun interface EndTick {
        fun onEndTick(player: PlayerEntity)
    }
}