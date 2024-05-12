package net.tropicbliss.tabgrabber.grabber;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.StyledNumberFormat;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.tropicbliss.tabgrabber.TabGrabber;
import net.tropicbliss.tabgrabber.config.ConfigManager;
import net.tropicbliss.tabgrabber.matcher.Formatter;
import net.tropicbliss.tabgrabber.mixin.PlayerListHudMixin;
import net.tropicbliss.tabgrabber.utils.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

interface ScoreboardKey {
    String getString();
}

public class PlayerTabManager {
    private static final Pattern NEWLINE = Pattern.compile("\n");
    private final MinecraftClient client = MinecraftClient.getInstance();
    private Scoreboard scoreboard;
    private ScoreboardObjective objective;
    private Formatter formatter;

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setObjective(ScoreboardObjective objective) {
        this.objective = objective;
    }

    private List<PlayerListEntry> collectPlayerEntries() {
        return ((PlayerListHudMixin) client.inGameHud.getPlayerListHud()).invokeCollectPlayerEntries();
    }

    private Text getHeader() {
        return ((PlayerListHudMixin) client.inGameHud.getPlayerListHud()).getHeader();
    }

    private Text getFooter() {
        return ((PlayerListHudMixin) client.inGameHud.getPlayerListHud()).getFooter();
    }

    private Map<ScoreboardKey, String> getScoreboardKeyPairs() {
        LinkedHashMap<ScoreboardKey, String> result = new LinkedHashMap<>();
        if (scoreboard != null) {
            List<PlayerListEntry> players = collectPlayerEntries();
            for (PlayerListEntry player : players) {
                String playerName = StringUtils.removeStyling(client.inGameHud.getPlayerListHud().getPlayerName(player));
                String formattedScore = null;
                if (objective != null) {
                    ScoreHolder scoreHolder = ScoreHolder.fromProfile(player.getProfile());
                    ReadableScoreboardScore readableScoreboardScore = scoreboard.getScore(scoreHolder, objective);
                    if (readableScoreboardScore != null) {
                        if (objective.getRenderType() != ScoreboardCriterion.RenderType.HEARTS) {
                            NumberFormat numberFormat = objective.getNumberFormatOr(StyledNumberFormat.YELLOW);
                            formattedScore = StringUtils.removeStyling(ReadableScoreboardScore.getFormattedScore(readableScoreboardScore, numberFormat));
                        } else {
                            formattedScore = String.valueOf(readableScoreboardScore.getScore());
                        }
                    }
                }
                result.put(new Player(playerName), formattedScore);
            }
        }
        Text header = getHeader();
        if (header != null) {
            result.put(new Metadata("Header"), StringUtils.removeStyling(header));
        }
        Text footer = getFooter();
        if (footer != null) {
            result.put(new Metadata("Footer"), StringUtils.removeStyling(footer));
        }
        return result;
    }

    public void updateFormatter(String domain) {
        try {
            formatter = ConfigManager.getConfig().serverConfigs.stream().filter(config -> config.domain.equals(domain)).findFirst().map(config -> Formatter.compile(config.format)).orElse(null);
        } catch (PatternSyntaxException e) {
            TabGrabber.LOGGER.warn("Invalid regex provided by the user");
            if (client.player != null) {
                Text text = Text.translatable("text.tabgrabber.invalid_regex").formatted(Formatting.RED);
                client.player.sendMessage(text);
            }
        }
    }

    public void clearFormatter() {
        formatter = null;
    }

    public Optional<String> getDebugInfo() {
        if (TabGrabber.isValidScene) {
            Map<ScoreboardKey, String> scoreboardInfo = getScoreboardKeyPairs();
            StringBuilder result = new StringBuilder();
            for (Map.Entry<ScoreboardKey, String> entry : scoreboardInfo.entrySet()) {
                result.append(entry.getKey().getString());
                result.append(": ");
                result.append(entry.getValue());
                result.append('\n');
            }
            StringUtils.removeLastNewline(result);
            return Optional.of(result.toString());
        }
        return Optional.empty();
    }

    public List<String> getHudInfo() {
        ArrayList<String> result = new ArrayList<>();
        if (formatter == null || !TabGrabber.enableHudRender) {
            return result;
        }
        Optional<String> debugInfo = getDebugInfo();
        if (debugInfo.isPresent()) {
            String data = debugInfo.get();
            String formatted = formatter.format(data);
            result = new ArrayList<>(Arrays.asList(NEWLINE.split(formatted)));
        }
        return result;
    }
}

final class Player implements ScoreboardKey {
    private final String keyName;

    public Player(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "Player{" +
                "keyName='" + keyName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ScoreboardKey)) {
            return false;
        }
        return toString().equals(o.toString());
    }

    @Override
    public String getString() {
        return keyName;
    }
}

final class Metadata implements ScoreboardKey {
    private final String keyName;

    public Metadata(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "keyName='" + keyName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ScoreboardKey)) {
            return false;
        }
        return toString().equals(o.toString());
    }

    @Override
    public String getString() {
        return keyName;
    }
}


