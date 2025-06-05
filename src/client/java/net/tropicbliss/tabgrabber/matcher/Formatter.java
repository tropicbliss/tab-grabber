package net.tropicbliss.tabgrabber.matcher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class Formatter {
    private final List<Token> tokens;

    private Formatter(String formatting) throws PatternSyntaxException, LexError {
        tokens = Tokenizer.tokenize(formatting);
    }

    public static Formatter compile(String formatting) throws PatternSyntaxException, LexError {
        return new Formatter(formatting);
    }

    public String format(String raw) {
        StringBuilder result = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();
        boolean skipCurrentLine = false;
        for (var token : tokens) {
            if (token instanceof Newline) {
                if (!skipCurrentLine) {
                    result.append(currentLine);
                    result.append('\n');
                }
                skipCurrentLine = false;
                currentLine = new StringBuilder();
            } else if (!skipCurrentLine) {
                if (token instanceof Plaintext tok) {
                    currentLine.append(tok.inner);
                } else if (token instanceof Regex tok) {
                    Matcher matcher = tok.inner.matcher(raw);
                    if (matcher.find()) {
                        currentLine.append(matcher.group());
                    } else {
                        skipCurrentLine = true;
                    }
                }
            }
        }
        result.append(currentLine);
        return result.toString();
    }
}
