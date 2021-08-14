package com.github.virusbear.metrix

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents

fun EntityMetrics(registry: MeterRegistry) {
    val count = registry.gauge(metrixIdentifier("entity.count"))
    val spawn = registry.counter(metrixIdentifier("entity.spawn"))
    val despawn = registry.counter(metrixIdentifier("entity.despawn"))

    ServerWorldEvents.LOAD.register(ServerWorldEvents.Load { _, world ->
        count.register(mapOf("dimension" to world.name)) {
            world.iterateEntities().count().toDouble()
        }
    })
    ServerWorldEvents.UNLOAD.register(ServerWorldEvents.Unload { _, world ->
        count -= mapOf("dimension" to world.name)
    })

    ServerEntityEvents.ENTITY_LOAD.register(ServerEntityEvents.Load { _, world ->
        spawn[mapOf("dimension" to world.name)].inc()
    })
    ServerEntityEvents.ENTITY_UNLOAD.register(ServerEntityEvents.Unload { _, world ->
        despawn[mapOf("dimension" to world.name)].inc()
    })
}