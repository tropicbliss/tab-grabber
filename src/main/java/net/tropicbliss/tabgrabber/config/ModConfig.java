package net.tropicbliss.tabgrabber.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.client.MinecraftClient;
import net.tropicbliss.tabgrabber.TabGrabber;
import net.tropicbliss.tabgrabber.matcher.Formatter;

import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

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

    @Override
    public void validatePostLoad() throws ValidationException {
        MinecraftClient client = MinecraftClient.getInstance();
        for (ServerConfig serverConfig : serverConfigs) {
            try {
                Formatter formatter = Formatter.compile(serverConfig.format);
                if (client.getCurrentServerEntry() != null) {
                    String currentDomain = client.getCurrentServerEntry().address;
                    if (currentDomain.equals(serverConfig.domain)) {
                        TabGrabber.tabManager.setFormatter(formatter);
                    }
                }
            } catch (PatternSyntaxException e) {
                throw new ValidationException("Invalid regex for the server: " + serverConfig.domain);
            }
        }
    }
}
