package com.github.virusbear.metrix

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.minecraft.entity.ItemEntity
import net.minecraft.util.registry.Registry

fun ItemMetrics(registry: MeterRegistry) {
    val count = registry.gauge(metrixIdentifier("item.count"))
    val spawn = registry.counter(metrixIdentifier("item.spawn"))
    val despawn = registry.counter(metrixIdentifier("item.despawn"))

    ServerWorldEvents.LOAD.register(ServerWorldEvents.Load { _, world ->
        Registry.ITEM.forEach { item ->
            count.register(mapOf("dimension" to world.name, "type" to Registry.ITEM.getId(item).toString())) {
                world.iterateEntities().filterIsInstance<ItemEntity>().filter { it.stack.item == item }.count().toDouble()
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
        if(entity !is ItemEntity)
            return@Load

        spawn[mapOf("dimension" to world.name, "type" to (Registry.ITEM.getId(entity.stack.item).toString()))].inc()
    })
    ServerEntityEvents.ENTITY_UNLOAD.register(ServerEntityEvents.Unload { entity, world ->
        if(entity !is ItemEntity)
            return@Unload

        despawn[mapOf("dimension" to world.name, "type" to (Registry.ITEM.getId(entity.stack.item).toString()))].inc()
    })
}