package com.unloadbrain.blog.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceIntegrationTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void showPostCreateEditPageTest() throws Exception {

        this.mockMvc.perform(get("/admin/post"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/post"))
                .andDo(print());
    }


    @Test
    public void createPostTest() throws Exception {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap();
        formData.put("title", Collections.singletonList("Hello!"));
        formData.put("content", Collections.singletonList("Hello <b>World!</b>"));
        formData.put("categories", Collections.singletonList("Java"));
        formData.put("tags", Collections.singletonList("java"));
        formData.put("action", Collections.singletonList("draft"));

        mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(formData));
    }

}
