package com.github.virusbear.metrix

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Metrix: ModInitializer {
    companion object {
        val LOG: Logger = LogManager.getLogger()
    }

    lateinit var server: MetrixServer

    override fun onInitialize() {
        LOG.info("Initializing Metrix Server")
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

        ServerLifecycleEvents.SERVER_STOPPED.register(ServerLifecycleEvents.ServerStopped {
            LOG.info("Stopping Metrix Server")
            server.stop()
        })

        LOG.info("Registering metrics")
        MetrixEvents.BIND_METERS.register(MetrixEvents.BindMeters { registry, server ->
            ChunkMetrics(registry)
            PlayerMetrics(registry, server)
            EntityMetrics(registry)
            ServerSizeMetrics(registry, server)
            TickMetrics(registry)
            MobMetrics(registry)
            ItemMetrics(registry)
        })


        ServerLifecycleEvents.SERVER_STARTED.register(ServerLifecycleEvents.ServerStarted {
            LOG.info("Starting Metrix Server")
            server.start()
        })

        LOG.info("Metrix initialized")
    }
}