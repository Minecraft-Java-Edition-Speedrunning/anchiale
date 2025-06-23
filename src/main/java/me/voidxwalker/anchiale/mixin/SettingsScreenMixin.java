package me.voidxwalker.anchiale.mixin;

import me.voidxwalker.anchiale.Anchiale;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SettingsScreen.class)
public abstract class SettingsScreenMixin extends Screen {
    @Unique
    private ButtonWidget locationButton;

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;<init>(IIIIILjava/lang/String;)V", ordinal = 0), slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=options.stream")))
    private String renameBroadcastButton(String string) {
        return Anchiale.buttonLocation.toString();
    }

    // modifyexpressionvalue on invoke <init> doesn't work and on new is before the string ldc for some reason
    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=options.stream")))
    private Object captureLocationButton(Object button) {
        locationButton = (ButtonWidget) button;
        return button;
    }

    @Inject(method = "buttonClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getTwitchStreamProvider()Lnet/minecraft/client/util/TwitchStreamProvider;"), cancellable = true)
    private void locationButtonClicked(ButtonWidget button, CallbackInfo ci) {
        Anchiale.ButtonLocation[] locations = Anchiale.ButtonLocation.values();
        Anchiale.buttonLocation = locations[(Anchiale.buttonLocation.ordinal() + 1) % locations.length];
        locationButton.message = Anchiale.buttonLocation.toString();
        Anchiale.saveConfig();
        ci.cancel();
    }
}
