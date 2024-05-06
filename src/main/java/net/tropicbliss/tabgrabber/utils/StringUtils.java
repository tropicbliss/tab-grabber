package net.tropicbliss.tabgrabber.utils;

import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern LITERAL_PATTERN = Pattern.compile("\\n");
    private static final Pattern STYLING_PATTERN = Pattern.compile("§[0-9a-fklmnors]");

    public static void removeLastNewline(StringBuilder sb) {
        int length = sb.length();
        if (length > 0 && sb.charAt(length - 1) == '\n') {
            sb.deleteCharAt(length - 1);
        }
    }

    public static String convertLiteralsToNewlines(String input) {
        Matcher matcher = LITERAL_PATTERN.matcher(input);
        return matcher.replaceAll("\n");
    }

    public static String removeStyling(Text styledText) {
        String unstyledText = styledText.getString();
        Matcher matcher = STYLING_PATTERN.matcher(unstyledText);
        String unformattedText = matcher.replaceAll("");
        return unformattedText.strip();
    }
}
