package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.exception.DraftPostNotFoundException;
import com.unloadbrain.blog.exception.InvalidPostStatusException;
import com.unloadbrain.blog.exception.PublishedPostNotFoundException;
import com.unloadbrain.blog.util.DateUtil;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class PublishPostServiceImpl extends AbstractPostService implements PublishPostService {

    private static final String CACHE_POST_BY_ID = "post";
    private static final String CACHE_POSTS_PAGE = "posts";
    private static final String CACHE_POST_PERMALINK_BY_ID = "postPermalink";

    private CategoryService categoryService;
    private TagService tagService;

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;

    private DateUtil dateUtil;
    private ModelMapper modelMapper;


    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CACHE_POSTS_PAGE, allEntries = true),
            @CacheEvict(cacheNames = CACHE_POST_BY_ID, key = "#postDTO.id", condition = "#postDTO.id != null"),
            @CacheEvict(cacheNames = CACHE_POST_PERMALINK_BY_ID, key = "#postDTO.id", condition = "#postDTO.id != null")
    })
    public PostIdentityDTO createUpdatePost(PostDTO postDTO) {

        Set<Category> categories = categoryService.getCategories(postDTO.getCategories());

        tagService.createUnsavedTags(postDTO.getTags());
        Set<Tag> tags =  tagService.getExistedTags(postDTO.getTags());

        setDefaultSummary(postDTO);
        setDefaultFeatureImageLink(postDTO);

        if (postDTO.getStatus() == CurrentPostStatus.NEW) {

            PublishedPost publishedPost = modelMapper.map(postDTO, PublishedPost.class);
            publishedPost.setPublishDate(dateUtil.getCurrentDate());
            publishedPost.setCategories(categories);
            publishedPost.setTags(tags);

            setDefaultPermalink(publishedPost);

            publishedPost = publishedPostRepository.save(publishedPost);

            return new PostIdentityDTO(publishedPost.getId(), CurrentPostStatus.PUBLISHED);

        } else if (postDTO.getStatus() == CurrentPostStatus.DRAFT) {

            Optional<DraftPost> draftOptional = draftPostRepository.findById(postDTO.getId());
            if (!draftOptional.isPresent()) {
                throw new DraftPostNotFoundException(
                        String.format("Could not found draft post id - %d", postDTO.getId()));
            }

            PublishedPost publishedPost = draftOptional.get().getPublishedPost();
            Long publishedPostId = null;
            if (publishedPost == null) {
                publishedPost = new PublishedPost();
                publishedPost.setPublishDate(dateUtil.getCurrentDate());
            } else {
                publishedPostId = publishedPost.getId();
            }
            modelMapper.map(postDTO, publishedPost);
            publishedPost.setId(publishedPostId);
            publishedPost.setCategories(categories);
            publishedPost.setTags(tags);

            setDefaultPermalink(publishedPost);

            publishedPost = publishedPostRepository.save(publishedPost);
            draftPostRepository.deleteById(postDTO.getId());

            return new PostIdentityDTO(publishedPost.getId(), CurrentPostStatus.PUBLISHED);

        } else if (postDTO.getStatus() == CurrentPostStatus.PUBLISHED) {

            Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(postDTO.getId());
            if (!publishedPostOptional.isPresent()) {
                throw new PublishedPostNotFoundException(
                        String.format("Could not found published post id - %d", postDTO.getId()));
            }

            PublishedPost publishedPost = publishedPostOptional.get();
            modelMapper.map(postDTO, publishedPost);
            publishedPost.setPublishDate(dateUtil.getCurrentDate());
            publishedPost.setCategories(categories);
            publishedPost.setTags(tags);

            setDefaultPermalink(publishedPost);

            publishedPost = publishedPostRepository.save(publishedPost);

            return new PostIdentityDTO(publishedPost.getId(), CurrentPostStatus.PUBLISHED);

        } else {
            throw new InvalidPostStatusException("Post current status should be either NEW, PUBLISHED or DRAFT.");
        }

    }

    private void setDefaultPermalink(PublishedPost publishedPost) {
        if (publishedPost.getPermalink() == null || publishedPost.getPermalink().length() == 0) {
            publishedPost.setPermalink(SlugUtil.toSlug(publishedPost.getTitle()));
        }
    }

    @Override
    @Cacheable(cacheNames = CACHE_POST_BY_ID, key = "#id")
    public PostDTO getPost(Long id) {

        Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(id);
        if (!publishedPostOptional.isPresent()) {
            throw new PublishedPostNotFoundException(String.format("Could not found published post id - %d", id));
        }

        PublishedPost publishedPost = publishedPostOptional.get();
        return modelMapper.map(publishedPost, PostDTO.class);

    }

    @Override
    @Cacheable(cacheNames = CACHE_POST_PERMALINK_BY_ID, key = "#postId")
    public String getPermalink(Long postId) {

        if (!publishedPostRepository.existsById(postId)) {
            throw new PublishedPostNotFoundException(String.format("Could not found published post id - %d", postId));
        }

        return publishedPostRepository.getPermalinkById(postId);
    }

    @Override
    @Cacheable(value = CACHE_POSTS_PAGE, key = "#pageable.pageNumber")
    public Page<PostDTO> getPosts(Pageable pageable) {

        Page<PublishedPost> postsPage = publishedPostRepository.findAll(pageable);
        List<PostDTO> postDTOList = postsPage.getContent().stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));

        return new PageImpl<>(postDTOList, pageable, postsPage.getTotalElements());
    }


}
