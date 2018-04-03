package com.unloadbrain.blog.repository;

import com.unloadbrain.blog.Application;
import com.unloadbrain.blog.BaseTest;
import com.unloadbrain.blog.domain.repository.PostRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class PostRepositoryTests extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private PostRepository postRepository;

    @Test
    public void checkBlackTitleThrowException() throws Exception {

    }

}