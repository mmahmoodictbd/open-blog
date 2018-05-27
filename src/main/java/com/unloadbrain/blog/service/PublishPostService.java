package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatusDTO;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.exception.DraftPostNotFoundException;
import com.unloadbrain.blog.exception.InvalidPostStatusException;
import com.unloadbrain.blog.exception.PublishedPostNotFoundException;
import com.unloadbrain.blog.util.DateUtility;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class PublishPostService extends AbstractPostService {

    private CategoryService categoryService;
    private TagService tagService;

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;

    private DateUtility dateUtility;
    private ModelMapper modelMapper;


    public PostIdentityDTO publishPost(PostDTO postDTO) {

        Set<Category> categories = categoryService.getCategories(postDTO.getCategories());
        Set<Tag> tags = tagService.getTags(postDTO.getTags());

        setDefaultSummary(postDTO);
        setDefaultFeatureImageLink(postDTO);

        if (postDTO.getId() == null) {

            PublishedPost publishedPost = modelMapper.map(postDTO, PublishedPost.class);
            publishedPost.setPublishDate(dateUtility.getCurrentDate());
            publishedPost.setCategories(categories);
            publishedPost.setTags(tags);

            setDefaultPermalink(publishedPost);

            publishedPost = publishedPostRepository.save(publishedPost);

            return new PostIdentityDTO(publishedPost.getId(), CurrentPostStatusDTO.PUBLISHED);
        }

        if (postDTO.getStatus() == CurrentPostStatusDTO.DRAFT) {

            Optional<DraftPost> draftOptional = draftPostRepository.findById(postDTO.getId());
            if (!draftOptional.isPresent()) {
                throw new DraftPostNotFoundException(
                        String.format("Could not found draft post id - %d", postDTO.getId()));
            }


            PublishedPost publishedPost = draftOptional.get().getPublishedPost();
            Long publishedPostId = null;
            if (publishedPost == null) {
                publishedPost = new PublishedPost();
                publishedPost.setPublishDate(dateUtility.getCurrentDate());
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

            return new PostIdentityDTO(publishedPost.getId(), CurrentPostStatusDTO.PUBLISHED);

        } else if (postDTO.getStatus() == CurrentPostStatusDTO.PUBLISHED) {

            Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(postDTO.getId());
            if (!publishedPostOptional.isPresent()) {
                throw new PublishedPostNotFoundException(
                        String.format("Could not found published post id - %d", postDTO.getId()));
            }

            PublishedPost publishedPost = publishedPostOptional.get();
            modelMapper.map(postDTO, publishedPost);
            publishedPost.setPublishDate(dateUtility.getCurrentDate());
            publishedPost.setCategories(categories);
            publishedPost.setTags(tags);

            setDefaultPermalink(publishedPost);

            publishedPost = publishedPostRepository.save(publishedPost);

            return new PostIdentityDTO(publishedPost.getId(), CurrentPostStatusDTO.PUBLISHED);

        } else {
            throw new InvalidPostStatusException("Post current status should be either PUBLISHED or DRAFT.");
        }

    }

    private void setDefaultPermalink(PublishedPost publishedPost) {
        if (publishedPost.getPermalink() == null || publishedPost.getPermalink().length() == 0) {
            publishedPost.setPermalink(SlugUtil.toSlug(publishedPost.getTitle()));
        }
    }

    public PostDTO getPublishedPost(Long id) {

        Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(id);
        if (!publishedPostOptional.isPresent()) {
            // TODO: Use custom exception
            throw new IllegalArgumentException("Post id not found.");
        }

        PublishedPost publishedPost = publishedPostOptional.get();
        return modelMapper.map(publishedPost, PostDTO.class);

    }

    public String getPublishedPostPermalink(Long postId) {
        return publishedPostRepository.getPermalinkById(postId);
    }

    public Page<PostDTO> getPublishedPosts(Pageable pageable) {

        Page<PublishedPost> postsPage = publishedPostRepository.findAll(pageable);
        List<PostDTO> postDTOList = postsPage.getContent().stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));

        return new PageImpl<>(postDTOList, pageable, postsPage.getTotalElements());
    }


}
