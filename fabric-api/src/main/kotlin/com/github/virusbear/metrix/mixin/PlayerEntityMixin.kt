package com.github.virusbear.metrix.mixin

import com.github.virusbear.metrix.PlayerTickEvents
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(PlayerEntity::class)
class PlayerEntityMixin {
    @Inject(at = [At("HEAD")], method = ["method_5773()V"])
    fun onStartPlayerTick(info: CallbackInfo) {
        PlayerTickEvents.START_PLAYER_TICK.invoker().onStartTick(this as PlayerEntity)
    }

    @Inject(at = [At("TAIL")], method = ["method_5773()V"])
    fun onEndPlayerTick(info: CallbackInfo) {
        PlayerTickEvents.END_PLAYER_TICK.invoker().onEndTick(this as PlayerEntity)
    }
}