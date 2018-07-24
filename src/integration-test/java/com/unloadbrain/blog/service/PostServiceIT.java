package com.unloadbrain.blog.service;

import com.unloadbrain.blog.util.UrlUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class PostServiceIT {

    private static final String POST_TITLE_IN_HTML = "placeholder=\"Title\" value=\"Hello World!\">";
    private static final String POST_SUMMARY_IN_HTML = "placeholder=\"Summary\" value=\"Hello World Summary\">";
    private static final String POST_CONTENT_IN_HTML = ">Hello <b>World!</b></textarea>";
    private static final String DRAFT_POST_STATUS_IN_HTML = "<input type=\"hidden\" id=\"status\" name=\"status\" value=\"DRAFT\"/>";
    private static final String POST_TAGS_IN_HTML = "value=\"java,programming\" data-role=\"tagsinput\"";
    private static final String POST_CATEGORIES_IN_HTML = "selected=\"selected\">Hello World</option>";
    private static final String POST_SEO_TITLE_IN_HTML = "placeholder=\"SEO Title\" value=\"SEO Title\">";
    private static final String POST_SEO_SLUG_IN_HTML = "placeholder=\"SEO Slug\" value=\"seo-hello-world\">";
    private static final String POST_SEO_DESCRIPTION_IN_HTML = "placeholder=\"SEO Description\" value=\"SEO description\">";
    private static final String POST_SEO_TAGS_IN_HTML = "placeholder=\"SEO Tags\" value=\"seotag\">";
    private static final String PUBLISHED_POST_STATUS_IN_HTML = "<input type=\"hidden\" id=\"status\" name=\"status\" value=\"PUBLISHED\"/>";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldShowPostCreateEditPage() throws Exception {

        mockMvc.perform(get("/admin/post"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/post"))
                .andExpect(content().string(containsString(
                        "<input type=\"hidden\" id=\"status\" name=\"status\" value=\"NEW\"/>"))
                );

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldCreateNewDraftPostTestAndReturnNewlyCreatedPost() throws Exception {

        // Given

        MultiValueMap<String, String> formData = buildPostFormData();
        formData.put("action", Collections.singletonList("DRAFT"));

        // When

        MvcResult result = mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(formData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/post?id={[0-9]+}&status=DRAFT"))
                .andReturn();

        // Then

        String createdPostURL = result.getResponse().getRedirectedUrl();
        mockMvc.perform(get(createdPostURL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(POST_TITLE_IN_HTML)))
                .andExpect(content().string(containsString(POST_SUMMARY_IN_HTML)))
                .andExpect(content().string(containsString(POST_CONTENT_IN_HTML)))
                .andExpect(content().string(containsString(DRAFT_POST_STATUS_IN_HTML)))
                .andExpect(content().string(containsString(POST_TAGS_IN_HTML)))
                .andExpect(content().string(containsString(POST_CATEGORIES_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_TITLE_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_SLUG_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_DESCRIPTION_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_TAGS_IN_HTML)));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldCreateNewPublishPostTestAndReturnNewlyCreatedPost() throws Exception {

        // Given

        MultiValueMap<String, String> formData = buildPostFormData();
        formData.put("action", Collections.singletonList("PUBLISH"));

        // When

        MvcResult result = mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(formData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/post?id={[0-9]+}&status=PUBLISHED"))
                .andReturn();

        // Then

        String createdPostURL = result.getResponse().getRedirectedUrl();
        mockMvc.perform(get(createdPostURL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(POST_TITLE_IN_HTML)))
                .andExpect(content().string(containsString(POST_SUMMARY_IN_HTML)))
                .andExpect(content().string(containsString(POST_CONTENT_IN_HTML)))
                .andExpect(content().string(containsString(PUBLISHED_POST_STATUS_IN_HTML)))
                .andExpect(content().string(containsString(POST_TAGS_IN_HTML)))
                .andExpect(content().string(containsString(POST_CATEGORIES_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_TITLE_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_SLUG_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_DESCRIPTION_IN_HTML)))
                .andExpect(content().string(containsString(POST_SEO_TAGS_IN_HTML)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldCreateNewPublishPostTestFromExistingDraftPost() throws Exception {

        // Given

        MultiValueMap<String, String> draftPostFormData = buildPostFormData();
        draftPostFormData.put("action", Collections.singletonList("DRAFT"));

        MvcResult draftPostMvcResult = mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(draftPostFormData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/post?id={[0-9]+}&status=DRAFT"))
                .andReturn();

        // When

        String createdDraftPostURL = draftPostMvcResult.getResponse().getRedirectedUrl();
        String draftPostId = UrlUtil.getQueryMap(createdDraftPostURL).get("id");

        MultiValueMap<String, String> publishedPostFormData = buildPostFormData();
        publishedPostFormData.put("id", Collections.singletonList(draftPostId));
        publishedPostFormData.put("title", Collections.singletonList("New title"));
        publishedPostFormData.put("status", Collections.singletonList("DRAFT"));
        publishedPostFormData.put("action", Collections.singletonList("PUBLISH"));

        MvcResult publishedPostMvcResult = mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(publishedPostFormData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/post?id={[0-9]+}&status=PUBLISHED"))
                .andReturn();

        // Then

        // Draft post should be deleted
        mockMvc.perform(get(createdDraftPostURL)).andExpect(status().isNotFound());

        String createdPublishedPostURL = publishedPostMvcResult.getResponse().getRedirectedUrl();
        mockMvc.perform(get(createdPublishedPostURL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("placeholder=\"Title\" value=\"New title\">")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void shouldCreateNewDraftPostTestFromExistingPublishedPost() throws Exception {

        // Given

        MultiValueMap<String, String> publishedPostFormData = buildPostFormData();
        publishedPostFormData.put("action", Collections.singletonList("PUBLISH"));

        MvcResult publishedPostMvcResult = mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(publishedPostFormData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/post?id={[0-9]+}&status=PUBLISHED"))
                .andReturn();

        // When

        String createdPublishedPostURL = publishedPostMvcResult.getResponse().getRedirectedUrl();
        String publishedPostId = UrlUtil.getQueryMap(createdPublishedPostURL).get("id");

        MultiValueMap<String, String> draftPostFormData = buildPostFormData();
        draftPostFormData.put("id", Collections.singletonList(publishedPostId));
        draftPostFormData.put("title", Collections.singletonList("New title"));
        draftPostFormData.put("status", Collections.singletonList("PUBLISHED"));
        draftPostFormData.put("action", Collections.singletonList("DRAFT"));

        MvcResult draftPostMvcResult = mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(draftPostFormData))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/post?id={[0-9]+}&status=DRAFT"))
                .andReturn();

        // Then

        // Both Draft and Published post should exists

        String createdDraftPostURL = draftPostMvcResult.getResponse().getRedirectedUrl();
        mockMvc.perform(get(createdDraftPostURL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("placeholder=\"Title\" value=\"New title\">")));

        mockMvc.perform(get(createdPublishedPostURL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(POST_TITLE_IN_HTML)));
    }

    private MultiValueMap<String, String> buildPostFormData() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap();
        formData.put("title", Collections.singletonList("Hello World!"));
        formData.put("content", Collections.singletonList("Hello <b>World!</b>"));
        formData.put("summary", Collections.singletonList("Hello World Summary"));
        formData.put("permalink", Collections.singletonList("hello-world"));
        formData.put("featureImageLink", Collections.singletonList("feature-image-link"));
        formData.put("status", Collections.singletonList("NEW"));
        formData.put("tags", Collections.singletonList("java,programming"));
        formData.put("categories", Collections.singletonList("Hello World"));
        formData.put("seoTitle", Collections.singletonList("SEO Title"));
        formData.put("seoSlug", Collections.singletonList("seo-hello-world"));
        formData.put("seoDescription", Collections.singletonList("SEO description"));
        formData.put("seoTags", Collections.singletonList("seotag"));
        return formData;
    }
}
