package com.unloadbrain.blog.service;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class PostServiceIT {

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
    public void showPostCreateEditPageTest() throws Exception {

        mockMvc.perform(get("/admin/post"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/post"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void createPostTest() throws Exception {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap();
        formData.put("title", Collections.singletonList("Hello!"));
        formData.put("content", Collections.singletonList("Hello <b>World!</b>"));
        formData.put("action", Collections.singletonList("DRAFT"));

        mockMvc.perform(post("/admin/post")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(formData))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/post?id=1&status=DRAFT"));
    }

}
