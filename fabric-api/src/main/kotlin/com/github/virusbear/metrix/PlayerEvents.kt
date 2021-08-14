package com.github.virusbear.metrix

import com.github.virusbear.metrix.PlayerEvents.PlayerJoin
import com.github.virusbear.metrix.PlayerEvents.PlayerLeave
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity

object PlayerEvents {
    @JvmStatic
    val PLAYER_JOIN = EventFactory.createArrayBacked(PlayerJoin::class.java) { listeners ->
        PlayerJoin { player ->
            listeners.forEach {
                it.onJoin(player)
            }
        }
    }

    @JvmStatic
    val PLAYER_LEAVE = EventFactory.createArrayBacked(PlayerLeave::class.java) { listeners ->
        PlayerLeave { player ->
            listeners.forEach {
                it.onLeave(player)
            }
        }
    }

    @FunctionalInterface
    fun interface PlayerJoin {
        fun onJoin(player: PlayerEntity)
    }

    @FunctionalInterface
    fun interface PlayerLeave {
        fun onLeave(player: PlayerEntity)
    }
}