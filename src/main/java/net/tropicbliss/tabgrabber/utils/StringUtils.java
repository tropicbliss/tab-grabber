package net.tropicbliss.tabgrabber.utils;

import net.minecraft.text.Text;

public class StringUtils {
    public static void removeLastNewline(StringBuilder sb) {
        int length = sb.length();
        if (length > 0 && sb.charAt(length - 1) == '\n') {
            sb.deleteCharAt(length - 1);
        }
    }

    public static String convertLiteralsToNewlines(String input) {
        return input.replace("\\n", "\n");
    }

    public static String removeStyling(Text styledText) {
        String unstyledText = styledText.getString();
        String unformattedText = unstyledText.replaceAll("§[0-9a-fklmnor]", "");
        return unformattedText.strip();
    }
}
