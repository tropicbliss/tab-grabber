package net.tropicbliss.tabgrabber.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "initWidgets")
    private void initWidgets(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("text.tabgrabber.mod_name"), b -> {
            if (this.client != null) {
                this.client.setScreen(ConfigManager.buildConfigScreen(this));
            }
        }).dimensions(10, 10, 90, 20).build());
    }
}
