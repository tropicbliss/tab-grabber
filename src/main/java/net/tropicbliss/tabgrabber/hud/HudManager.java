package net.tropicbliss.tabgrabber.hud;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.config.ModConfig;
import net.tropicbliss.tabgrabber.grabber.PlayerTabManager;

import java.util.List;

public class HudManager {
    private static final int LINE_HEIGHT = 10;

    public static void register() {
        MinecraftClient instance = MinecraftClient.getInstance();
        ModConfig config = ConfigManager.getConfig();

        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            List<String> lines = PlayerTabManager.getHudInfo();
            if (!lines.isEmpty()) {
                MatrixStack matrixStack = ctx.getMatrices();
                HudUtils utils = new HudUtils(lines);
                Coordinates coordinates = utils.push(matrixStack);
                int x = coordinates.x();
                int y = coordinates.y();
                for (String line : lines) {
                    HudUtils.LineUtils lineUtils = utils.getLineUtilsInstance(line);
                    int offset = lineUtils.getTextAlignmentOffset();
                    if (config.textBackground && !line.isEmpty()) {
                        ctx.fill(x - 1 + offset, y - 1, x + lineUtils.getLineLength() + offset, y + LINE_HEIGHT - 1, 0x80000000);
                    }
                    ctx.drawText(instance.textRenderer, line, x + offset, y, config.textColor, config.textShadow);
                    y += LINE_HEIGHT;
                }
                utils.pop(matrixStack);
            }
        });
    }
}
