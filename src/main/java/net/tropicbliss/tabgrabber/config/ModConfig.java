package net.tropicbliss.tabgrabber.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.tropicbliss.tabgrabber.TabGrabber;

import java.util.ArrayList;

@Config(name = TabGrabber.MOD_ID)
public class ModConfig implements ConfigData {
    public boolean enable = true;

    @ConfigEntry.BoundedDiscrete(max = 100)
    public int x = 5;
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int y = 5;

    @ConfigEntry.ColorPicker
    public int textColor = 0xFFFFFF;

    public boolean textBackground = false;

    @ConfigEntry.BoundedDiscrete(min = 50, max = 150)
    public int textScale = 100;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public TextAlignment textAlignment = TextAlignment.Left;

    public boolean textShadow = true;

    public ArrayList<ServerConfig> serverConfigs = new ArrayList<>();

    public enum TextAlignment {
        Left,
        Center,
        Right
    }

    public static class ServerConfig {
        public String domain = "";
        @ConfigEntry.Gui.PrefixText
        public String format = "";
    }
}
