package net.tropicbliss.tabgrabber.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.config.ModConfig;

import java.util.List;

class HudUtils {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final TextRenderer textRenderer = client.textRenderer;
    private static final Window window = client.getWindow();

    private static final int SCALE = 1;
    private static final int PADDING = 4;

    private final List<String> lines;
    private int boxWidth = 0;

    public HudUtils(List<String> lines) {
        this.lines = lines;
        lines.stream().map(textRenderer::getWidth).max(Integer::compare).ifPresent(width -> boxWidth = width);
    }

    public Coordinates getCoordinates() {
        int x = calculateXAxis();
        int y = calculateYAxis();
        return new Coordinates(x, y);
    }

    private int calculateXAxis() {
        int configX = ConfigManager.getConfig().x;
        int screenWidth = window.getScaledWidth();
        int adjustedWidth = Math.round((float) screenWidth - PADDING - boxWidth * SCALE);
        int calculatedValue = Math.round((float) adjustedWidth / SCALE * configX / 100);
        if (SCALE < 1) {
            int adjustedScreenWidth = Math.round((float) screenWidth * SCALE);
            int maximumX = PADDING;
            int minimumX = Math.min(screenWidth - adjustedScreenWidth - PADDING, maximumX);
            return Math.max(Math.max(calculatedValue, minimumX), maximumX);
        } else {
            int adjustedLineWidth = Math.round((float) boxWidth * SCALE);
            return Math.min(Math.max(calculatedValue, PADDING), screenWidth - PADDING - adjustedLineWidth);
        }
    }

    private int calculateYAxis() {
        int configY = ConfigManager.getConfig().y;
        int lineHeight = textRenderer.fontHeight;
        int size = lines.size();
        int screenHeight = window.getScaledHeight();

        int adjustedHeight = Math.round((float) screenHeight - PADDING - lineHeight * size * SCALE);
        int calculatedValue = Math.round((float) adjustedHeight / SCALE * configY / 100);
        if (SCALE < 1) {
            return Math.max(calculatedValue, PADDING);
        } else {
            int adjustedLineHeight = Math.round((float) lineHeight * size * SCALE);
            return Math.min(Math.max(calculatedValue, PADDING), screenHeight - adjustedLineHeight - PADDING);
        }
    }

    public LineUtils getLineUtilsInstance(String line) {
        return this.new LineUtils(line);
    }

    class LineUtils {
        private final int lineLength;

        private LineUtils(String line) {
            lineLength = textRenderer.getWidth(line);
        }

        public int getLineLength() {
            return lineLength;
        }

        public int getTextAlignmentOffset() {
            int offset = 0;
            ModConfig.TextAlignment textAlignment = ConfigManager.getConfig().textAlignment;
            switch (textAlignment) {
                case Right -> offset = boxWidth - lineLength;
                case Center -> offset = (boxWidth - lineLength) / 2;
            }
            return offset;
        }
    }
}
