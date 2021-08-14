package com.github.virusbear.metrix

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents

fun TickMetrics(registry: MeterRegistry) {
    val time = registry.timer(metrixIdentifier("tick.time"))
    val count = registry.counter(metrixIdentifier("tick.count"))

    ServerTickEvents.START_SERVER_TICK.register(ServerTickEvents.StartTick {
        count[tags("server", "")].inc()
        time.start(tags("server", ""))
    })
    ServerTickEvents.END_SERVER_TICK.register(ServerTickEvents.EndTick {
        time.stop(tags("server", ""))
    })

    ServerTickEvents.START_WORLD_TICK.register(ServerTickEvents.StartWorldTick {
        count[tags("world", it.name)].inc()
        time.start(tags("world", it.name))
    })
    ServerTickEvents.END_WORLD_TICK.register(ServerTickEvents.EndWorldTick {
        time.stop(tags("world", it.name))
    })

    PlayerTickEvents.START_PLAYER_TICK.register(PlayerTickEvents.StartTick {
        count[tags("player", it.gameProfile.name)].inc()
        time.start(tags("player", it.gameProfile.name))
    })
    PlayerTickEvents.END_PLAYER_TICK.register(PlayerTickEvents.EndTick {
        time.stop(tags("player", it.gameProfile.name))
    })

    ServerWorldEvents.UNLOAD.register(ServerWorldEvents.Unload { _, world ->
        count -= tags("world", world.name)
        time -= tags("world", world.name)
    })
    PlayerEvents.PLAYER_LEAVE.register(PlayerEvents.PlayerLeave {
        count -= tags("player", it.gameProfile.name)
        time -= tags("player", it.gameProfile.name)
    })
}

private fun tags(type: String, id: String): Tags =
    mapOf("type" to type, "id" to id)