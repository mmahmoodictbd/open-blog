package com.unloadbrain.blog.util;

import lombok.experimental.UtilityClass;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@UtilityClass
public class SlugUtil {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {

        String noWhitespaceString = WHITESPACE.matcher(input).replaceAll("-");
        String normalizedString = Normalizer.normalize(noWhitespaceString, Normalizer.Form.NFD);
        String onlyLatinString = NON_LATIN.matcher(normalizedString).replaceAll("");

        if (onlyLatinString.length() > 0 && onlyLatinString.charAt(onlyLatinString.length() - 1) == '-') {
            onlyLatinString = onlyLatinString.substring(0, onlyLatinString.length() - 1);
        }

        return onlyLatinString.toLowerCase(Locale.ENGLISH);
    }

}
