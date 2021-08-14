package com.github.virusbear.metrix

import net.minecraft.server.MinecraftServer

fun PlayerMetrics(registry: MeterRegistry, server: MinecraftServer) {
    val count = registry.gauge(metrixIdentifier("player.count"))
    val health = registry.gauge(metrixIdentifier("player.health"))
    val food = registry.gauge(metrixIdentifier("player.food"))
    val saturation = registry.gauge(metrixIdentifier("player.saturation"))

    count.register {
        server.currentPlayerCount.toDouble()
    }

    PlayerEvents.PLAYER_JOIN.register(PlayerEvents.PlayerJoin { player ->
        health.register(mapOf("player" to player.gameProfile.name)) {
            player.health.toDouble()
        }
        food.register(mapOf("player" to player.gameProfile.name)) {
            player.hungerManager.foodLevel.toDouble()
        }
        saturation.register(mapOf("player" to player.gameProfile.name)) {
            player.hungerManager.saturationLevel.toDouble()
        }
    })
    PlayerEvents.PLAYER_LEAVE.register(PlayerEvents.PlayerLeave { player ->
        health -= mapOf("player" to player.gameProfile.name)
        food -= mapOf("player" to player.gameProfile.name)
        saturation -= mapOf("player" to player.gameProfile.name)
    })
}