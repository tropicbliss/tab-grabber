package net.tropicbliss.tabgrabber.matcher;

import net.tropicbliss.tabgrabber.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

sealed interface Segment extends RawSegment permits Regex, Str {
}

sealed interface RawSegment permits Segment, NewLine {
}

public class Formatter {
    private final List<ArrayList<Segment>> segments = new ArrayList<>();

    private Formatter(String formatting) throws PatternSyntaxException {
        Stack<RawSegment> segmentsWithNewlines = new Stack<>();
        String[] rawSegments = formatting.split("(?<=})|(?=\\{)");
        for (String segment : rawSegments) {
            if (segment.startsWith("{") && segment.endsWith("}")) {
                segmentsWithNewlines.add(new Regex(segment.substring(1, segment.length() - 1)));
            } else {
                String[] lineSegments = segment.split("\n");
                if (lineSegments.length == 1) {
                    segmentsWithNewlines.add(new Str(lineSegments[0]));
                } else {
                    for (String lineSegment : lineSegments) {
                        if (!lineSegment.isEmpty()) {
                            segmentsWithNewlines.add(new Str(lineSegment));
                        }
                        segmentsWithNewlines.add(new NewLine());
                    }
                    if (!segment.endsWith("\n")) {
                        segmentsWithNewlines.pop();
                    }
                }
            }
        }
        ArrayList<Segment> line = new ArrayList<>();
        for (RawSegment segment : segmentsWithNewlines) {
            if (segment instanceof NewLine) {
                segments.add(line);
                line = new ArrayList<>();
            } else {
                line.add((Segment) segment);
            }
        }
        if (!line.isEmpty()) {
            segments.add(line);
        }
    }

    public static Formatter compile(String formatting) throws PatternSyntaxException {
        return new Formatter(formatting);
    }

    public String format(String raw) {
        StringBuilder result = new StringBuilder();
        outer:
        for (ArrayList<Segment> line : segments) {
            StringBuilder resultLine = new StringBuilder();
            for (Segment segment : line) {
                if (segment instanceof Str seg) {
                    resultLine.append(seg.inner());
                } else if (segment instanceof Regex seg) {
                    Matcher matcher = seg.inner.matcher(raw);
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
        StringUtils.removeLastNewline(result);
        return result.toString();
    }
}

final class Regex implements Segment {
    public final Pattern inner;

    public Regex(String regex) throws PatternSyntaxException {
        this.inner = Pattern.compile(regex);
    }
}

record Str(String inner) implements Segment {
}

final class NewLine implements RawSegment {
}
