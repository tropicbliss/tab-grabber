package net.tropicbliss.tabgrabber.listeners;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.tropicbliss.tabgrabber.grabber.PlayerTabManager;

public class ServerJoinEvent {
    public static void register() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            if (server.getCurrentServerEntry() != null) {
                PlayerTabManager.updateFormatter(server.getCurrentServerEntry().address);
            }
        }));
    }
}
