package net.tropicbliss.tabgrabber.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.gui.screen.Screen;

public class ConfigManager {
    public static void register() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
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
