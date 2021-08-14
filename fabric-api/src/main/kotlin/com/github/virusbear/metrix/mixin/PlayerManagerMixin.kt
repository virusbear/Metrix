package com.github.virusbear.metrix.mixin

import com.github.virusbear.metrix.PlayerEvents
import net.minecraft.network.ClientConnection
import net.minecraft.server.PlayerManager
import net.minecraft.server.network.ServerPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PlayerManager::class)
class PlayerManagerMixin {
    @Inject(at = [At("TAIL")], method = ["onPlayerConnect"])
    fun onPlayerJoin(connection: ClientConnection, player: ServerPlayerEntity, info: CallbackInfo) {
        PlayerEvents.PLAYER_JOIN.invoker().onJoin(player)
    }

    @Inject(at = [At("TAIL")], method = ["remove"])
    fun onPlayerLeave(player: ServerPlayerEntity, info: CallbackInfo) {
        PlayerEvents.PLAYER_LEAVE.invoker().onLeave(player)
    }
}