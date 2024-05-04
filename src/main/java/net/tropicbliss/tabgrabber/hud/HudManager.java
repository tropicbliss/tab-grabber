package net.tropicbliss.tabgrabber.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.tropicbliss.tabgrabber.TabGrabber;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.config.ModConfig;

import java.util.ArrayList;

public class HudManager {
    private static final int LINE_HEIGHT = 10;

    public static void register() {
        MinecraftClient instance = MinecraftClient.getInstance();
        ModConfig config = ConfigManager.getConfig();

        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            if (TabGrabber.enableHudRender) {
                ArrayList<String> lines = TabGrabber.tabManager.getHudInfo();
                MatrixStack matrixStack = ctx.getMatrices();
                matrixStack.push();
                matrixStack.translate(5, 5, 0);
                matrixStack.scale(1, 1, 1);
                matrixStack.translate(-5, -5, 0);
                HudUtils utils = new HudUtils(lines);
                Coordinates coordinates = utils.getCoordinates();
                int x = coordinates.x();
                int y = coordinates.y();
                for (String line : lines) {
                    HudUtils.LineUtils lineUtils = utils.getLineUtilsInstance(line);
                    if (config.textBackground) {
                        ctx.fill(x - 1, y - 1, x + lineUtils.getLineLength(), y + LINE_HEIGHT - 1, 0x80000000);
                    }
                    int offset = lineUtils.getTextAlignmentOffset();
                    ctx.drawText(instance.textRenderer, line, x + offset, y, config.textColor, config.textShadow);
                    y += LINE_HEIGHT;
                }
                matrixStack.pop();
            }
        });
    }
}
