package com.unloadbrain.blog.util;

/**
 * OpenBlog System related utils
 */
public class OpenBlogSystemUtil {

    /**
     * Constant for user's home directory key
     */
    private static final String USER_HOME = "user.home";

    /**
     * OpenBlog home directory under user home
     */
    private static final String OPENBLOG_HOME = ".openblog";

    /**
     * Return user home directory path.
     * Example: /Users/mossaddeque
     */
    public String getUserHome() {
        return System.getProperty(USER_HOME);
    }

    /**
     * Return OpenBlog home directory path.
     * Example: /Users/mossaddeque/.openblog
     */
    public String getOpenBlogHome() {
        return System.getProperty(USER_HOME) + "/" + OPENBLOG_HOME;
    }
}
