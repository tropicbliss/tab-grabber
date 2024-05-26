package net.tropicbliss.tabgrabber;

import net.fabricmc.api.ClientModInitializer;
import net.tropicbliss.tabgrabber.command.DebugCommand;
import net.tropicbliss.tabgrabber.command.HelpCommand;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.grabber.PlayerTabManager;
import net.tropicbliss.tabgrabber.hud.HudManager;
import net.tropicbliss.tabgrabber.keybind.KeyInputHandler;
import net.tropicbliss.tabgrabber.listeners.ServerJoinEvent;
import net.tropicbliss.tabgrabber.listeners.ServerLeaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TabGrabber implements ClientModInitializer {
    public static final String MOD_ID = "tab-grabber";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        ConfigManager.register();
        DebugCommand.register();
        HelpCommand.register();
        HudManager.register();
        ServerJoinEvent.register();
        ServerLeaveEvent.register();
        PlayerTabManager.register();

        LOGGER.info("Hello from Tab Grabber!");
    }
}
