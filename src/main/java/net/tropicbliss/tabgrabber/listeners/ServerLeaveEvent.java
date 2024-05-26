package net.tropicbliss.tabgrabber.listeners;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.tropicbliss.tabgrabber.grabber.PlayerTabManager;

public class ServerLeaveEvent {
    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register(((handler, server) -> PlayerTabManager.clearFormatter()));
    }
}
