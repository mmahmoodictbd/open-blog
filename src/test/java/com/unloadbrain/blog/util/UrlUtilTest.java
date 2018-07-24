package com.unloadbrain.blog.util;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UrlUtilTest {

    @Test
    public void shouldReturnUrlParamsFromAnUrl() {

        Map<String, String> params = UrlUtil.getQueryMap("/admin/post?id=1&status=PUBLISHED");
        assertEquals("1", params.get("id"));
        assertEquals("PUBLISHED", params.get("status"));
    }

    @Test
    public void shouldReturnEmptyUrlParamMapFromAnUrl() {

        Map<String, String> params = UrlUtil.getQueryMap("/admin/post");
        assertTrue(params.isEmpty());
    }

}