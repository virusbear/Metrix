package com.github.virusbear.metrix

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class MetrixAPI: ModInitializer {
    companion object {
        val LOG: Logger = LogManager.getLogger()
    }

    override fun onInitialize() {
        LOG.info("Metrix API initialized")
    }
}