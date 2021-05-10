package org.code.neighborhood;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {
    private static final String HEX_WEBCOLOR_PATTERN
            = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
    private static final Pattern pattern = Pattern.compile(HEX_WEBCOLOR_PATTERN);
    private static final Set<String> colors = Set.of("white", "silver", "gray", "black", "red", "maroon", "yellow", "olive", "lime", "green", "aqua", "teal", "blue", "navy", "fuchsia", "purple");
    public static boolean isColor(String color) {
        Matcher matcher = pattern.matcher(color);
        return matcher.matches() || colors.contains(color);
    }
}