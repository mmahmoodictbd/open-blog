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

        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String onlyLatin = NON_LATIN.matcher(normalized).replaceAll("");

        String removedExtraHyphen = onlyLatin.charAt(onlyLatin.length() - 1) == '-' ?
                onlyLatin.substring(0, onlyLatin.length() - 1) : onlyLatin;

        return removedExtraHyphen.toLowerCase(Locale.ENGLISH);
    }

}
