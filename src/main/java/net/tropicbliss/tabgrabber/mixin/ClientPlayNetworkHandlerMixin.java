package net.tropicbliss.tabgrabber.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import net.tropicbliss.tabgrabber.grabber.PlayerTabManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onPlayerListHeader", at = @At("HEAD"))
    private void onHeaderFooterPacket(PlayerListHeaderS2CPacket pkt, CallbackInfo ci) {
        PlayerTabManager.newPacketReceived();
    }

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At("HEAD"))
    private void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket pkt, CallbackInfo ci) {
        PlayerTabManager.newPacketReceived();
    }

    @Inject(method = "onScoreboardScoreReset", at = @At("HEAD"))
    private void onScoreboardScoreReset(ScoreboardScoreResetS2CPacket pkt, CallbackInfo ci) {
        PlayerTabManager.newPacketReceived();
    }

    @Inject(method = "onScoreboardDisplay", at = @At("HEAD"))
    private void onScoreboardDisplay(ScoreboardDisplayS2CPacket pkt, CallbackInfo ci) {
        PlayerTabManager.newPacketReceived();
    }

    @Inject(method = "onTeam", at = @At("HEAD"))
    private void onTeam(TeamS2CPacket pkt, CallbackInfo ci) {
        PlayerTabManager.newPacketReceived();
    }
}
