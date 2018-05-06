package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.Post;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.dto.PostActionDTO;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.dto.PostStatusDTO;
import com.unloadbrain.blog.util.DateUtility;
import com.unloadbrain.blog.util.SlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class PostService {

    private static final String COMMA = ",";

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;

    private DateUtility dateUtility;
    private ModelMapper modelMapper;

    public PostService(PublishedPostRepository postRepository,
                       DraftPostRepository draftPostRepository,
                       CategoryRepository categoryRepository,
                       TagRepository tagRepository,
                       DateUtility dateUtility,
                       ModelMapper modelMapper) {
        this.publishedPostRepository = postRepository;
        this.draftPostRepository = draftPostRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.dateUtility = dateUtility;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public PostIdentityDTO createUpdatePost(PostDTO postDTO) {

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

        if (postDTO.getId() == null) {

            if (PostActionDTO.DRAFT == postDTO.getAction()) {

                DraftPost draftPost = modelMapper.map(post, DraftPost.class);
                draftPost = draftPostRepository.save(draftPost);

                return new PostIdentityDTO(draftPost.getId(), PostStatusDTO.DRAFT);

            } else if (PostActionDTO.PUBLISH == postDTO.getAction()) {

                PublishedPost publishedPost = modelMapper.map(post, PublishedPost.class);
                publishedPost.setPublishDate(dateUtility.getCurrentDate());
                publishedPost = publishedPostRepository.save(publishedPost);

                return new PostIdentityDTO(publishedPost.getId(), PostStatusDTO.PUBLISHED);
            }

        } else {

            if (PostActionDTO.DRAFT == postDTO.getAction()) {

                if (postDTO.getStatus() == PostStatusDTO.DRAFT) {

                    Optional<DraftPost> draftOptional = draftPostRepository.findById(postDTO.getId());
                    if (!draftOptional.isPresent()) {
                        // TODO: Throw exception
                    }

                    DraftPost draftPost = draftOptional.get();
                    modelMapper.map(post, draftPost);
                    draftPost = draftPostRepository.save(draftPost);

                    return new PostIdentityDTO(draftPost.getId(), PostStatusDTO.DRAFT);

                } else if (postDTO.getStatus() == PostStatusDTO.PUBLISHED) {

                    Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(postDTO.getId());
                    if (!publishedPostOptional.isPresent()) {
                        // TODO: Throw exception
                    }

                    DraftPost draftPost = modelMapper.map(post, DraftPost.class);
                    draftPost.setPublishedPost(publishedPostOptional.get());
                    draftPostRepository.save(draftPost);

                    return new PostIdentityDTO(draftPost.getId(), PostStatusDTO.DRAFT);

                } else {
                    // TODO: Throw exception
                }

            } else if (PostActionDTO.PUBLISH == postDTO.getAction()) {

                if (postDTO.getStatus() == PostStatusDTO.DRAFT) {

                    Optional<DraftPost> draftOptional = draftPostRepository.findById(postDTO.getId());
                    if (!draftOptional.isPresent()) {
                        // TODO: Throw exception
                    }

                    PublishedPost publishedPost = draftOptional.get().getPublishedPost();
                    if (publishedPost == null) {
                        publishedPost = new PublishedPost();
                        publishedPost.setPublishDate(dateUtility.getCurrentDate());
                    }
                    modelMapper.map(post, publishedPost);
                    publishedPostRepository.save(publishedPost);
                    draftPostRepository.deleteById(postDTO.getId());

                    return new PostIdentityDTO(publishedPost.getId(), PostStatusDTO.PUBLISHED);

                } else if (postDTO.getStatus() == PostStatusDTO.PUBLISHED) {

                    Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(postDTO.getId());
                    if (!publishedPostOptional.isPresent()) {
                        // TODO: Throw exception
                    }

                    PublishedPost publishedPost = publishedPostOptional.get();
                    modelMapper.map(post, publishedPost);
                    publishedPost.setPublishDate(dateUtility.getCurrentDate());
                    publishedPostRepository.save(publishedPost);

                    return new PostIdentityDTO(publishedPost.getId(), PostStatusDTO.PUBLISHED);

                } else {
                    // TODO: Throw exception
                }

            }

        }

        // TODO: Add custom exception
        throw new IllegalStateException("Invalid post creation state.");

    }

}
