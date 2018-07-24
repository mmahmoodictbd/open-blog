package com.unloadbrain.blog.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class UrlUtil {

    public static Map<String, String> getQueryMap(String url) {

        int queryStringStartIndex = url.indexOf("?");
        String queryString = queryStringStartIndex != -1? url.substring(queryStringStartIndex + 1) : "";

        if (StringUtils.isEmpty(queryString)) {
            return Collections.emptyMap();
        }

        String[] params = queryString.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }

        return map;
    }


}
