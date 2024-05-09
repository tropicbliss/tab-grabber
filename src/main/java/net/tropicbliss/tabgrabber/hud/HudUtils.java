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
    private static final ModConfig config = ConfigManager.getConfig();

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
        int configX = config.x;
        int screenWidth = window.getScaledWidth();
        float scale = getScale();
        int adjustedWidth = Math.round((float) screenWidth - PADDING - boxWidth * scale);
        int calculatedValue = Math.round((float) adjustedWidth / scale * configX / 100);
        if (scale < 1) {
            int adjustedScreenWidth = Math.round((float) screenWidth * scale);
            int maximumX = PADDING;
            int minimumX = Math.min(screenWidth - adjustedScreenWidth - PADDING, maximumX);
            return Math.max(Math.max(calculatedValue, minimumX), maximumX);
        } else {
            int adjustedLineWidth = Math.round((float) boxWidth * scale);
            return Math.min(Math.max(calculatedValue, PADDING), screenWidth - PADDING - adjustedLineWidth);
        }
    }

    private int calculateYAxis() {
        int configY = config.y;
        int lineHeight = textRenderer.fontHeight;
        int size = lines.size();
        int screenHeight = window.getScaledHeight();
        float scale = getScale();
        int adjustedHeight = Math.round((float) screenHeight - PADDING - lineHeight * size * scale);
        int calculatedValue = Math.round((float) adjustedHeight / scale * configY / 100);
        if (scale < 1) {
            return Math.max(calculatedValue, PADDING);
        } else {
            int adjustedLineHeight = Math.round((float) lineHeight * size * scale);
            return Math.min(Math.max(calculatedValue, PADDING), screenHeight - adjustedLineHeight - PADDING);
        }
    }

    public static float getScale() {
        return (float) config.textScale / 100;
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
            ModConfig.TextAlignment textAlignment = config.textAlignment;
            switch (textAlignment) {
                case Right -> offset = boxWidth - lineLength;
                case Center -> offset = (boxWidth - lineLength) / 2;
            }
            return offset;
        }
    }
}
