package com.github.virusbear.metrix

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents

fun ChunkMetrics(registry: MeterRegistry) {
    val count = registry.gauge(metrixIdentifier("chunk.count"))
    val load = registry.counter(metrixIdentifier("chunk.load"))
    val unload = registry.counter(metrixIdentifier("chunk.unload"))

    ServerChunkEvents.CHUNK_LOAD.register(ServerChunkEvents.Load { world, _ ->
        load[mapOf("dimension" to world.name)].inc()
    })
    ServerChunkEvents.CHUNK_UNLOAD.register(ServerChunkEvents.Unload { world, _ ->
        unload[mapOf("dimension" to world.name)].inc()
    })

    ServerWorldEvents.LOAD.register(ServerWorldEvents.Load { _, world ->
        count.register(mapOf("dimension" to world.name)) {
            world.chunkManager.loadedChunkCount.toDouble()
        }
    })
    ServerWorldEvents.UNLOAD.register(ServerWorldEvents.Unload { _, world ->
        count -= mapOf("dimension" to world.name)
    })
}