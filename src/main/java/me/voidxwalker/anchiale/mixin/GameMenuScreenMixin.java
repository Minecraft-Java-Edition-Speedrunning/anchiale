package me.voidxwalker.anchiale.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import me.voidxwalker.anchiale.Anchiale;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 0), index = 1)
    private int titleScreenX(int x, @Share("originalX") LocalIntRef originalX) {
        originalX.set(x);
        return x;
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIILjava/lang/String;)V", ordinal = 0), index = 2)
    private int titleScreenY(int y, @Share("originalY") LocalIntRef originalY) {
        originalY.set(y);
        return y;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addAnchialeButton(CallbackInfo ci, @Share("originalX") LocalIntRef originalX, @Share("originalY") LocalIntRef originalY) {
        int x, y;
        switch (Anchiale.buttonLocation) {
            case CENTER:
                x = originalX.get();
                y = originalY.get() + 24;
                break;
            case BOTTOM_RIGHT:
            default:
                x = this.width - 104;
                y = this.height - 24;
                break;
        }
        if (this.client.isInSingleplayer()) {
            this.buttons.add(new ButtonWidget(1507, x, y, Anchiale.buttonLocation == Anchiale.ButtonLocation.BOTTOM_RIGHT ? 100 : 200, 20, "Quit to Title"));
        }
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"))
    private void onMenuQuitWorldClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 1507) {
            Anchiale.fastReset = true;
            button.id = 1;
        }
    }

    @Inject(method = "buttonClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;<init>()V"))
    private void fastResetFalse(CallbackInfo ci) {
        Anchiale.fastReset = false;
    }
}
