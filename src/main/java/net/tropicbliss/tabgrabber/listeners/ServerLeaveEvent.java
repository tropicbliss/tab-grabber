package net.tropicbliss.tabgrabber.listeners;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.tropicbliss.tabgrabber.TabGrabber;

public class ServerLeaveEvent {
    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> TabGrabber.tabManager.onServerLeave()));
    }
}
