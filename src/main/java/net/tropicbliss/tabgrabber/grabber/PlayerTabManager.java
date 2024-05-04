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
import java.util.regex.PatternSyntaxException;

public class PlayerTabManager {
    private Scoreboard scoreboard;
    private ScoreboardObjective objective;
    private final MinecraftClient client = MinecraftClient.getInstance();

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

    private LinkedHashMap<ScoreboardKey, String> getScoreboardKeyPairs() {
        LinkedHashMap<ScoreboardKey, String> result = new LinkedHashMap<>();
        if (scoreboard != null) {
            List<PlayerListEntry> players = collectPlayerEntries();
            for (PlayerListEntry player : players) {
                String playerName = removeStyling(client.inGameHud.getPlayerListHud().getPlayerName(player));
                String formattedScore = null;
                if (objective != null) {
                    ScoreHolder scoreHolder = ScoreHolder.fromProfile(player.getProfile());
                    ReadableScoreboardScore readableScoreboardScore = scoreboard.getScore(scoreHolder, objective);
                    if (readableScoreboardScore != null) {
                        if (objective.getRenderType() != ScoreboardCriterion.RenderType.HEARTS) {
                            NumberFormat numberFormat = objective.getNumberFormatOr(StyledNumberFormat.YELLOW);
                            formattedScore = removeStyling(ReadableScoreboardScore.getFormattedScore(readableScoreboardScore, numberFormat));
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
            result.put(new Metadata("Header"), removeStyling(header));
        }
        Text footer = getFooter();
        if (footer != null) {
            result.put(new Metadata("Footer"), removeStyling(footer));
        }
        return result;
    }

    public void onServerJoin(String domain) {
        try {
            formatter = ConfigManager.getConfig().serverConfigs.stream().filter(config -> config.domain.equals(domain)).findFirst().map(config -> Formatter.compile(config.format)).orElse(null);
        } catch (PatternSyntaxException e) {
            if (client.player != null) {
                Text text = Text.translatable("text.tabgrabber.invalid_regex").formatted(Formatting.RED);
                client.player.sendMessage(text);
            }
        }
    }

    public void onServerLeave() {
        formatter = null;
    }

    public Optional<String> getDebugInfo() {
        if (TabGrabber.isValidScene && !client.isInSingleplayer()) {
            HashMap<ScoreboardKey, String> scoreboardInfo = getScoreboardKeyPairs();
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

    private static String removeStyling(Text styledText) {
        String unstyledText = styledText.getString();
        String unformattedText = unstyledText.replaceAll("§[0-9a-fklmnor]", "");
        return unformattedText.strip();
    }

    public ArrayList<String> getHudInfo() {
        ArrayList<String> result = new ArrayList<>();
        if (formatter == null || !TabGrabber.enableHudRender) {
            return result;
        }
        Optional<String> debugInfo = getDebugInfo();
        if (debugInfo.isPresent()) {
            String data = debugInfo.get();
            String formatted = formatter.format(data);
            result = new ArrayList<>(Arrays.asList(formatted.split("\n")));
        }
        return result;
    }
}
