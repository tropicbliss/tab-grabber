package net.tropicbliss.tabgrabber.utils;

public class StringUtils {
    public static void removeLastNewline(StringBuilder sb) {
        int length = sb.length();
        if (length > 0 && sb.charAt(length - 1) == '\n') {
            sb.deleteCharAt(length - 1);
        }
    }
}
