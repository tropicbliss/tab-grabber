package net.tropicbliss.tabgrabber.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

class Tokenizer {
    public static List<Token> tokenize(String text) throws PatternSyntaxException, LexError {
        text = escapeSequenceReplacement(text);
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            char currentChar = text.charAt(i);
            if (currentChar == '\n') {
                tokens.add(new Newline());
                i++;
            } else if (currentChar == '{') {
                LexResult result = collectBraceGroup(text, i);
                tokens.add(new Regex(result.content()));
                i = result.position();
            } else if (currentChar == '}') {
                throw new LexError("Unmatched closing brace");
            } else {
                LexResult result = collectLiteral(text, i);
                if (!result.content().isEmpty()) {
                    tokens.add(new Plaintext(result.content()));
                }
                i = result.position();
            }
        }
        return tokens;
    }

    private static LexResult collectBraceGroup(String text, int start) throws LexError {
        if (text.charAt(start) != '{') {
            throw new IllegalArgumentException("Expected '{'");
        }
        int i = start + 1;
        int braceCount = 1;
        while (i < text.length()) {
            char currentChar = text.charAt(i);
            if (currentChar == '\\' && i + 1 < text.length() &&
                    (text.charAt(i + 1) == '{' || text.charAt(i + 1) == '}')) {
                i += 2;
            } else if (currentChar == '{') {
                braceCount++;
                i++;
            } else if (currentChar == '}') {
                braceCount--;
                if (braceCount == 0) {
                    String content = text.substring(start + 1, i);
                    return new LexResult(content, i + 1);
                }
                i++;
            } else {
                i++;
            }
        }
        throw new LexError("Unclosed brace group");
    }

    private static LexResult collectLiteral(String text, int start) {
        StringBuilder result = new StringBuilder();
        int i = start;
        while (i < text.length()) {
            char currentChar = text.charAt(i);
            if (currentChar == '\\' && i + 1 < text.length() &&
                    (text.charAt(i + 1) == '{' || text.charAt(i + 1) == '}')) {
                result.append(text.charAt(i + 1));
                i += 2;
            } else if (currentChar == '\n' || currentChar == '{' || currentChar == '}') {
                break;
            } else {
                result.append(currentChar);
                i++;
            }
        }
        return new LexResult(result.toString(), i);
    }

    private static String escapeSequenceReplacement(String input) {
        return input.replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\'", "'")
                .replace("\\\"", "\"")
                .replace("\\r", "\r")
                .replace("\\\\", "\\")
                .replace("\\f", "\f")
                .replace("\\b", "\b");
    }
}
