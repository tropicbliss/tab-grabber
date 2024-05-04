package net.tropicbliss.tabgrabber;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.tropicbliss.tabgrabber.command.DebugCommand;
import net.tropicbliss.tabgrabber.command.HelpCommand;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.config.ModConfig;
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

    public static PlayerTabManager tabManager = new PlayerTabManager();

    public static boolean enableHudRender = false;
    public static boolean isValidScene = false;

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        ConfigManager.register();
        DebugCommand.register();
        HelpCommand.register();
        HudManager.register();
        ServerJoinEvent.register();
        ServerLeaveEvent.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            enableHudRender = false;
            isValidScene = false;
            tabManager.setScoreboard(null);
            tabManager.setObjective(null);

            if (client.player != null && client.world != null) {
                Scoreboard scoreboard = client.world.getScoreboard();
                ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.LIST);
                ModConfig config = ConfigManager.getConfig();

                if (!client.isInSingleplayer() || client.player.networkHandler.getListedPlayerListEntries().size() > 1 || scoreboardObjective != null) {
                    isValidScene = true;
                    tabManager.setScoreboard(scoreboard);
                    tabManager.setObjective(scoreboardObjective);
                    if (config.enable)
                        enableHudRender = true;
                }
            }
        });
        LOGGER.info("Hello from Tab Grabber!");
    }
}
