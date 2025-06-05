package net.tropicbliss.tabgrabber.matcher;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class Formatter {
    private final List<Token> tokens;
    private final String randomString = UUID.randomUUID().toString();

    private Formatter(String formatting) throws PatternSyntaxException, LexError {
        tokens = Tokenizer.tokenize(formatting);
    }

    public static Formatter compile(String formatting) throws PatternSyntaxException, LexError {
        return new Formatter(formatting);
    }

    public List<String> format(String raw) {
        StringBuilder result = new StringBuilder();
        for (var token : tokens) {
            if (token instanceof Plaintext tok) {
                result.append(tok.inner);
            } else if (token instanceof Newline) {
                result.append('\n');
            } else if (token instanceof Regex tok) {
                Matcher matcher = tok.inner.matcher(raw);
                if (matcher.find()) {
                    result.append(matcher.group());
                } else {
                    result.append(randomString);
                }
            }
        }
        return Arrays.stream(result.toString().split("\n")).filter(line -> !line.contains(randomString)).toList();
    }
}
