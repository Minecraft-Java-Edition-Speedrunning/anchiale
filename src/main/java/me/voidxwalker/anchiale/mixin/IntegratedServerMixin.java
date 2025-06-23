package me.voidxwalker.anchiale.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.voidxwalker.anchiale.Anchiale;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.Future;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin {
    @WrapWithCondition(method = "stopRunning", at = @At(value = "INVOKE", target = "Lcom/google/common/util/concurrent/Futures;getUnchecked(Ljava/util/concurrent/Future;)Ljava/lang/Object;"))
    public boolean fastReset(Future<?> e) {
        if (Anchiale.fastReset) {
            Anchiale.LOGGER.info("Exiting world without waiting for server tasks to finish");
            return false;
        }
        return true;
    }
}
