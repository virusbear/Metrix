package com.github.virusbear.metrix.mixin

import com.github.virusbear.metrix.PlayerTickEvents
import com.github.virusbear.metrix.ServerWorldTickEvents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ServerWorld::class)
class ServerWorldMixin {
    @Inject(at = [At("HEAD")], method = ["method_18765(Ljava/util/function/BooleanSupplier;)V"])
    fun onStartServerWorldTick(info: CallbackInfo) {
        ServerWorldTickEvents.START_SERVER_WORLD_TICK.invoker().onStartTick(this as ServerWorld)
    }

    @Inject(at = [At("TAIL")], method = ["method_18765(Ljava/util/function/BooleanSupplier;)V"])
    fun onEndPlayerTick(info: CallbackInfo) {
        ServerWorldTickEvents.END_SERVER_WORLD_TICK.invoker().onEndTick(this as ServerWorld)
    }
}