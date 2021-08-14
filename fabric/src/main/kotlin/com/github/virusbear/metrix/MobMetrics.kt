package com.github.virusbear.metrix

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

fun MobMetrics(registry: MeterRegistry) {
    val count = registry.gauge(metrixIdentifier("mob.count"))
    val spawn = registry.counter(metrixIdentifier("mob.spawn"))
    val despawn = registry.counter(metrixIdentifier("mob.despawn"))

    ServerWorldEvents.LOAD.register(ServerWorldEvents.Load { _, world ->
        Registry.ENTITY_TYPE.filter { it.spawnGroup == SpawnGroup.MONSTER }.forEach { type ->
            count.register(mapOf("dimension" to world.name, "type" to Registry.ENTITY_TYPE.getId(type).toString())) {
                world.iterateEntities().filter { it.type == type }.count().toDouble()
            }
        }
    })
    ServerWorldEvents.UNLOAD.register(ServerWorldEvents.Unload { _, world ->
        count.tags().filter {
            it["dimension"] == world.name
        }.forEach {
            count -= it
        }
    })

    ServerEntityEvents.ENTITY_LOAD.register(ServerEntityEvents.Load { entity, world ->
        if(entity.type.spawnGroup != SpawnGroup.MONSTER)
            return@Load

        spawn[mapOf("dimension" to world.name, "type" to (Registry.ENTITY_TYPE.getId(entity.type).toString()))].inc()
    })
    ServerEntityEvents.ENTITY_UNLOAD.register(ServerEntityEvents.Unload { entity, world ->
        if(entity.type.spawnGroup != SpawnGroup.MONSTER)
            return@Unload

        despawn[mapOf("dimension" to world.name, "type" to (Registry.ENTITY_TYPE.getId(entity.type).toString()))].inc()
    })
}