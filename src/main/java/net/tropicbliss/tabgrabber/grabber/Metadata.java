package net.tropicbliss.tabgrabber.grabber;

import java.util.Objects;

final class Metadata implements ScoreboardKey {
    private final String keyName;

    public Metadata(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "keyName='" + keyName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ScoreboardKey)) {
            return false;
        }
        return toString().equals(o.toString());
    }

    @Override
    public String getString() {
        return keyName;
    }
}
