package net.tropicbliss.tabgrabber.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

sealed interface Token extends InternalToken permits Plaintext, Regex {
}

sealed interface InternalToken permits Token, Newline {
}

class Tokenizer {
    private static final Pattern BRACE_PAIR = Pattern.compile("(?<!\\\\)\\{[^}]*(?<!\\\\)}");

    public static List<List<Token>> tokenize(String input) throws PatternSyntaxException {
        String canonicalInput = escapeSequenceReplacement(input);
        List<String> rawSegments = splitWithDelimiters(canonicalInput);
        List<List<Token>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        for (String rawSegment : rawSegments) {
            if (rawSegment.startsWith("{") && rawSegment.endsWith("}")) {
                result.get(result.size() - 1).add(new Regex(rawSegment.substring(1, rawSegment.length() - 1)));
            } else {
                rawSegment = rawSegment.replace("\\{", "{");
                rawSegment = rawSegment.replace("\\}", "}");
                List<InternalToken> lineSegments = delineateNewlines(rawSegment);
                for (InternalToken token : lineSegments) {
                    if (token instanceof Plaintext plaintext) {
                        result.get(result.size() - 1).add(plaintext);
                    } else {
                        result.add(new ArrayList<>());
                    }
                }
            }
        }
        return result;
    }

    public static List<InternalToken> delineateNewlines(String input) {
        List<InternalToken> result = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '\n') {
                if (!buffer.isEmpty()) {
                    result.add(new Plaintext(buffer.toString()));
                    buffer.setLength(0);
                }
                result.add(new Newline());
            } else {
                buffer.append(c);
            }
        }
        if (!buffer.isEmpty()) {
            result.add(new Plaintext(buffer.toString()));
        }
        return result;
    }

    private static List<String> splitWithDelimiters(String input) {
        List<String> result = new ArrayList<>();
        Matcher matcher = BRACE_PAIR.matcher(input);
        int start = 0;
        while (matcher.find()) {
            if (start != matcher.start()) {
                result.add(input.substring(start, matcher.start()));
            }
            result.add(matcher.group());
            start = matcher.end();
        }
        if (start < input.length()) {
            result.add(input.substring(start));
        }
        return result;
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

final class Newline implements InternalToken {
}

final class Plaintext implements Token {
    public String inner;

    public Plaintext(String inner) {
        this.inner = inner;
    }
}

final class Regex implements Token {
    public Pattern inner;

    public Regex(String inner) {
        this.inner = Pattern.compile(inner);
    }
}
