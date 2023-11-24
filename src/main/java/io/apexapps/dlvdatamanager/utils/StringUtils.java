package io.apexapps.dlvdatamanager.utils;

import java.util.function.Function;

public class StringUtils {
    /**
     * Capitalizes the first character in the string.
     */
    public static String capitalize(String s) {
        return stringTransformer(s, "capitalize", String::toUpperCase);
    }

    private static String stringTransformer(String s, String operationDescription, Function<String, String> transformation) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException(String.format("You cannot %s an empty string", operationDescription));
        }
        return transformation.apply(s.substring(0, 1)) + s.substring(1);
    }

    public static String screamingToCamelCase(String original) {
        StringBuilder sb = new StringBuilder();
        String[] parts = original.toLowerCase().split("_");
        for (int i = 0; i < parts.length; i++) {
            sb.append(i == 0 ? parts[i] : capitalize(parts[i]));
        }
        return sb.toString();
    }
}
