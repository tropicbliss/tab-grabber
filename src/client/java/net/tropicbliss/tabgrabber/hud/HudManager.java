package net.tropicbliss.tabgrabber.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.tropicbliss.tabgrabber.TabGrabber;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.config.ModConfig;
import net.tropicbliss.tabgrabber.grabber.PlayerTabManager;
import org.joml.Matrix3x2fStack;

import java.util.List;

public class HudManager {
    private static final int LINE_HEIGHT = 10;

    public static void register() {
        MinecraftClient instance = MinecraftClient.getInstance();
        ModConfig config = ConfigManager.getConfig();

        HudElementRegistry.addLast(Identifier.of(TabGrabber.MOD_ID, "hud"), ((ctx, tickCounter) -> {
            List<String> lines = PlayerTabManager.getHudInfo();
            if (!lines.isEmpty()) {
                Matrix3x2fStack matrixStack = ctx.getMatrices();
                HudUtils utils = new HudUtils(lines);
                Coordinates coordinates = utils.push(matrixStack);
                int x = coordinates.x();
                int y = coordinates.y();
                for (String line : lines) {
                    HudUtils.LineUtils lineUtils = utils.getLineUtilsInstance(line);
                    int offset = lineUtils.getTextAlignmentOffset();
                    if (config.textBackground && !line.isEmpty()) {
                        ctx.fill(x - 1 + offset, y - 1, x + lineUtils.getLineLength() + offset, y + LINE_HEIGHT - 1,
                                -2147483648);
                    }
                    ctx.drawText(instance.textRenderer, line, x + offset, y, ColorHelper.withAlpha(255, config.textColor), config.textShadow);
                    y += LINE_HEIGHT;
                }
                utils.pop(matrixStack);
            }
        }));
    }
}