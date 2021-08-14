package com.github.virusbear.metrix

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader

class Metrix: ModInitializer {
    lateinit var server: MetrixServer

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(ServerLifecycleEvents.ServerStarting { minecraftServer ->
            server = MetrixServer(MetrixBinder {
                MetrixEvents.BIND_METERS.invoker().onBindMeter(this, minecraftServer)
            }, MetrixConfig.load(
                FabricLoader
                    .getInstance()
                    .configDir
                    .resolve( "metrix.properties")
                    .toFile()
            ))
        })

        ServerLifecycleEvents.SERVER_STOPPED.register(ServerLifecycleEvents.ServerStopped { _ ->
            server.stop()
        })

        MetrixEvents.BIND_METERS.register(MetrixEvents.BindMeters { registry, server ->
            ChunkMetrics(registry)
            PlayerMetrics(registry, server)
            EntityMetrics(registry)
            ServerSizeMetrics(registry, server)
            TickMetrics(registry)
            MobMetrics(registry)
            ItemMetrics(registry)
        })
    }
}