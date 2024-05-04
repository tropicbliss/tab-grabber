package net.tropicbliss.tabgrabber.listeners;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.tropicbliss.tabgrabber.TabGrabber;

public class ServerJoinEvent {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> TabGrabber.tabManager.onServerJoin(server.getServerIp())));
    }
}
