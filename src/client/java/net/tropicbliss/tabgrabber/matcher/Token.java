package net.tropicbliss.tabgrabber.matcher;

import java.util.regex.Pattern;

sealed interface Token permits Newline, Plaintext, Regex {
}

final class Newline implements Token {
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
