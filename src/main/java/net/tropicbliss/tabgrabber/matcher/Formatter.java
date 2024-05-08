package net.tropicbliss.tabgrabber.matcher;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class Formatter {
    private final List<List<Token>> tokens;

    private Formatter(String formatting) throws PatternSyntaxException {
        tokens = Tokenizer.tokenize(formatting);
    }

    public static Formatter compile(String formatting) throws PatternSyntaxException {
        return new Formatter(formatting);
    }

    public String format(String raw) {
        StringBuilder result = new StringBuilder();
        outer:
        for (List<Token> line : tokens) {
            StringBuilder resultLine = new StringBuilder();
            for (Token token : line) {
                if (token instanceof Plaintext tok) {
                    resultLine.append(tok.inner);
                } else if (token instanceof Regex tok) {
                    Matcher matcher = tok.inner.matcher(raw);
                    if (matcher.find()) {
                        if (matcher.groupCount() >= 1) {
                            resultLine.append(matcher.group(1));
                        } else {
                            resultLine.append(matcher.group());
                        }
                    } else {
                        continue outer;
                    }
                }
            }
            result.append(resultLine);
            result.append('\n');
        }
        return result.toString().stripTrailing();
    }
}
