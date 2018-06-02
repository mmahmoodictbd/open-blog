package com.unloadbrain.blog.repository;

import com.unloadbrain.blog.Application;
import com.unloadbrain.blog.BaseTest;
import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.TagRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
@ActiveProfiles("it")
public class DraftPostRepositoryIT extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private DraftPostRepository draftPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    //TODO; Write the test
    @Test
    public void checkBlackTitleThrowException() {

    }

    @Test
    @Transactional
    public void testDraftPostSavesCorrectly() {

        // Given

        categoryRepository.deleteAll();
        tagRepository.deleteAll();
        draftPostRepository.deleteAll();

        Category category = new Category();
        category.setName("Programming");
        category.setSlug("programming");
        category = categoryRepository.saveAndFlush(category);

        Tag tag = new Tag();
        tag.setName("Java");
        tag.setSlug("java");
        tag = tagRepository.saveAndFlush(tag);

        DraftPost post = new DraftPost();
        post.setTitle("Hello World to Java!");
        post.setContent("Long detail content.");
        post.setCategories(Collections.singleton(category));
        post.setTags(Collections.singleton(tag));
        draftPostRepository.save(post);

        // When
        Optional<DraftPost> retrievedPost = draftPostRepository.findById(post.getId());

        // Then
        assertTrue(retrievedPost.isPresent());
    }
}