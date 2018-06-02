package com.unloadbrain.blog.repository;

import com.unloadbrain.blog.Application;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
@ActiveProfiles("it")
public class DataJpaAuditIT {

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void create() {

        Tag unsaved = new Tag();
        unsaved.setName("sampleTag");
        unsaved.setSlug("sample-tag");
        Tag saved = tagRepository.save(unsaved);

        assertNotNull(saved.getCreatedAt());
    }


}
