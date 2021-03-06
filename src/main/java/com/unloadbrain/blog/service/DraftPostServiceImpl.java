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
import com.unloadbrain.blog.dto.TagDTO;
import com.unloadbrain.blog.exception.DraftPostNotFoundException;
import com.unloadbrain.blog.exception.InvalidPostStatusException;
import com.unloadbrain.blog.exception.PublishedPostNotFoundException;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
@Transactional
public class DraftPostServiceImpl extends AbstractPostService implements DraftPostService {



    private CategoryService categoryService;
    private TagService tagService;

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;

    private ModelMapper modelMapper;

    @Override
    public PostIdentityDTO draftPost(PostDTO postDTO) {

        Set<Category> categories = categoryService.getCategories(postDTO.getCategories());

        tagService.createUnsavedTags(postDTO.getTags());
        Set<Tag> tags =  tagService.getExistedTags(postDTO.getTags());

        setDefaultSummary(postDTO);
        setDefaultFeatureImageLink(postDTO);

        if (postDTO.getStatus() == CurrentPostStatus.NEW) {

            DraftPost draftPost = modelMapper.map(postDTO, DraftPost.class);
            draftPost.setCategories(categories);
            draftPost.setTags(tags);

            setDefaultPermalink(draftPost);

            draftPost = draftPostRepository.save(draftPost);

            return new PostIdentityDTO(draftPost.getId(), CurrentPostStatus.DRAFT);

        } else if (postDTO.getStatus() == CurrentPostStatus.DRAFT) {

            Optional<DraftPost> draftOptional = draftPostRepository.findById(postDTO.getId());
            if (!draftOptional.isPresent()) {
                throw new DraftPostNotFoundException(
                        String.format("Could not found draft post id - %d", postDTO.getId()));
            }

            DraftPost draftPost = draftOptional.get();
            modelMapper.map(postDTO, draftPost);
            draftPost.setCategories(categories);
            draftPost.setTags(tags);

            setDefaultPermalink(draftPost);

            draftPost = draftPostRepository.save(draftPost);

            return new PostIdentityDTO(draftPost.getId(), CurrentPostStatus.DRAFT);

        } else if (postDTO.getStatus() == CurrentPostStatus.PUBLISHED) {

            Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(postDTO.getId());
            if (!publishedPostOptional.isPresent()) {
                throw new PublishedPostNotFoundException(
                        String.format("Could not found published post id - %d", postDTO.getId()));
            }

            DraftPost draftPost = modelMapper.map(postDTO, DraftPost.class);
            draftPost.setId(null);
            draftPost.setPublishedPost(publishedPostOptional.get());
            draftPost.setCategories(categories);
            draftPost.setTags(tags);

            setDefaultPermalink(draftPost);

            draftPost = draftPostRepository.save(draftPost);

            return new PostIdentityDTO(draftPost.getId(), CurrentPostStatus.DRAFT);

        } else {
            throw new InvalidPostStatusException("Post current status should be either NEW, PUBLISHED or DRAFT.");
        }

    }

    private void setDefaultPermalink(DraftPost draftPost) {
        if (draftPost.getPermalink() == null || draftPost.getPermalink().length() == 0) {
            draftPost.setPermalink(SlugUtil.toSlug(draftPost.getTitle()));
        }
    }

}
