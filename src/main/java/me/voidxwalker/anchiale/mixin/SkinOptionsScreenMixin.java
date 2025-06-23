package me.voidxwalker.anchiale.mixin;

import me.voidxwalker.anchiale.Anchiale;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkinOptionsScreen.class)
public abstract class SkinOptionsScreenMixin extends Screen {
    @Unique
    private ButtonWidget locationButton;

    @Inject(method = "init", at = @At("TAIL"))
    private void addLocationButton(CallbackInfo ci) {
        this.buttons.add(this.locationButton = new ButtonWidget(1541, this.width / 2 - 155, this.height / 6 + 120, 150, 20, Anchiale.buttonLocation.toString()));
    }

    @Inject(method = "buttonClicked", at = @At("TAIL"))
    private void locationButtonClicked(ButtonWidget button, CallbackInfo ci) {
        if (button.active && button.id == 1541) {
            Anchiale.ButtonLocation[] locations = Anchiale.ButtonLocation.values();
            Anchiale.buttonLocation = locations[(Anchiale.buttonLocation.ordinal() + 1) % locations.length];
            locationButton.message = Anchiale.buttonLocation.toString();
            Anchiale.saveConfig();
        }
    }
}
