package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.Post;
import com.unloadbrain.blog.domain.model.PostStatus;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.domain.repository.PostRepository;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.util.DateUtility;
import com.unloadbrain.blog.util.SlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;


@Service
public class PostService {

    private static final String COMMA = ",";
    private static final String CREATE_POST_ACTION_DRAFT = "draft";
    private static final String CREATE_POST_ACTION_PUBLISH = "publish";

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;

    private DateUtility dateUtility;
    private ModelMapper modelMapper;

    public PostService(PostRepository postRepository,
                       CategoryRepository categoryRepository,
                       TagRepository tagRepository,
                       DateUtility dateUtility,
                       ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.dateUtility = dateUtility;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Post createPost(PostDTO postDTO) {

        Post post = modelMapper.map(postDTO, Post.class);

        Set<Category> categories = new LinkedHashSet<>();
        for (String categoryString : postDTO.getCategories().split(COMMA)) {
            Category persistedCategory = categoryRepository.findByName(categoryString);
            if (persistedCategory == null) {
                // TODO: Categories will be pre persisted
                Category category = new Category();
                category.setName(categoryString);
                category.setSlug(SlugUtil.toSlug(categoryString));
                persistedCategory = categoryRepository.save(category);
            }
            categories.add(persistedCategory);
        }
        post.setCategories(categories);

        Set<Tag> tags = new LinkedHashSet<>();
        for (String tagString : postDTO.getTags().split(COMMA)) {
            Tag persistedTag = tagRepository.findByName(tagString);
            if (persistedTag == null) {
                Tag tag = new Tag();
                tag.setName(tagString);
                tag.setSlug(SlugUtil.toSlug(tagString));
                persistedTag = tagRepository.save(tag);
            }
            tags.add(persistedTag);
        }
        post.setTags(tags);

        post.setPermalink(SlugUtil.toSlug(post.getTitle()));

        if (CREATE_POST_ACTION_DRAFT.equals(postDTO.getAction())) {
            post.setStatus(PostStatus.DRAFT);
        } else if (CREATE_POST_ACTION_PUBLISH.equals(postDTO.getAction())) {
            post.setStatus(PostStatus.PUBLISHED);
            post.setPublishDate(dateUtility.getCurrentDate());
        }

        postRepository.save(post);
        return post;
    }

}
