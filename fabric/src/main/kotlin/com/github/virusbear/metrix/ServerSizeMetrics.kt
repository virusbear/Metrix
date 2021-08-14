package com.github.virusbear.metrix

import net.minecraft.server.MinecraftServer
import java.io.File

fun ServerSizeMetrics(registry: MeterRegistry, server: MinecraftServer) {
    registry.gauge(metrixIdentifier("server.size")).register {
        folderSize(server.runDirectory).toDouble()
    }
}

private fun folderSize(obj: File): Long =
    obj.walkTopDown().toList().sumOf { it.length() }