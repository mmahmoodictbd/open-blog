package com.unloadbrain.blog.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SlugUtilTest {

    @Test
    public void toSlugReplaceWhitespaceWithHyphenTest() {
        assertEquals("hello-world", SlugUtil.toSlug("hello world"));
    }

    @Test
    public void toSlugNormalizeTest() {
        assertEquals("hello-world", SlugUtil.toSlug("hellô world"));
    }

    @Test
    public void toSlugRemoveNonLatinAndRemoveLastHyphenTest() {
        assertEquals("hello-world", SlugUtil.toSlug("hello world مرحبا"));
    }

    @Test
    public void toSlugtoLowercaseTest() {
        assertEquals("hello-world", SlugUtil.toSlug("Hello World"));
    }
}