package net.tropicbliss.tabgrabber.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;
import net.tropicbliss.tabgrabber.grabber.PlayerTabManager;

public class ConfigManager {
    public static void register() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((modConfigConfigHolder, config) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getCurrentServerEntry() != null) {
                PlayerTabManager.updateFormatter(client.getCurrentServerEntry().address);
            }
            return ActionResult.PASS;
        });
    }

    public static ModConfig getConfig() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static Screen buildConfigScreen(Screen parent) {
        return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
    }

    public static void toggleGuiOption() {
        ModConfig config = getConfig();
        config.enable = !config.enable;
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
