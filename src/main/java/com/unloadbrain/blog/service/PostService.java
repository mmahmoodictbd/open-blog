package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Category;
import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.CategoryRepository;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.dto.PostActionDTO;
import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import com.unloadbrain.blog.dto.PostListDTO;
import com.unloadbrain.blog.dto.PostStatusDTO;
import com.unloadbrain.blog.util.DateUtility;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PostService {

    private static final String COMMA = ",";

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;
    private PostRepository postRepository;

    private DateUtility dateUtility;
    private ModelMapper modelMapper;

    @Transactional
    public PostIdentityDTO createUpdatePost(PostDTO postDTO) {

        Set<Category> categories = new LinkedHashSet<>();

        if (postDTO.getCategories() != null && postDTO.getCategories().length() > 0) {

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
        }


        Set<Tag> tags = new LinkedHashSet<>();

        if (postDTO.getTags() != null && postDTO.getTags().length() > 0) {

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
        }

        if (postDTO.getFeatureImageLink() == null || postDTO.getFeatureImageLink().trim().length() == 0) {
            Document content = Jsoup.parse(postDTO.getContent());
            Element firstImage = content.getElementsByTag("img").first();
            if (firstImage != null) {
                String absUrl = firstImage.attr("src");
                if(absUrl != null && absUrl.trim().length() > 0) {
                    int index = absUrl.indexOf("/files/");
                    if (index > 0) {
                        String relativeUrl = absUrl.substring(index);
                        postDTO.setFeatureImageLink(relativeUrl);
                    }
                }
            }
        }

        if (postDTO.getId() == null) {

            if (PostActionDTO.DRAFT == postDTO.getAction()) {

                DraftPost draftPost = modelMapper.map(postDTO, DraftPost.class);
                draftPost.setCategories(categories);
                draftPost.setTags(tags);

                if (draftPost.getPermalink() == null || draftPost.getPermalink().length() == 0) {
                    draftPost.setPermalink(SlugUtil.toSlug(draftPost.getTitle()));
                }

                draftPost = draftPostRepository.save(draftPost);

                return new PostIdentityDTO(draftPost.getId(), PostStatusDTO.DRAFT);

            } else if (PostActionDTO.PUBLISH == postDTO.getAction()) {

                PublishedPost publishedPost = modelMapper.map(postDTO, PublishedPost.class);
                publishedPost.setPublishDate(dateUtility.getCurrentDate());
                publishedPost.setCategories(categories);
                publishedPost.setTags(tags);

                if (publishedPost.getPermalink() == null || publishedPost.getPermalink().length() == 0) {
                    publishedPost.setPermalink(SlugUtil.toSlug(publishedPost.getTitle()));
                }

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
                    modelMapper.map(postDTO, draftPost);
                    draftPost.setCategories(categories);
                    draftPost.setTags(tags);

                    if (draftPost.getPermalink() == null || draftPost.getPermalink().length() == 0) {
                        draftPost.setPermalink(SlugUtil.toSlug(draftPost.getTitle()));
                    }

                    draftPost = draftPostRepository.save(draftPost);

                    return new PostIdentityDTO(draftPost.getId(), PostStatusDTO.DRAFT);

                } else if (postDTO.getStatus() == PostStatusDTO.PUBLISHED) {

                    Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(postDTO.getId());
                    if (!publishedPostOptional.isPresent()) {
                        // TODO: Throw exception
                    }

                    DraftPost draftPost = modelMapper.map(postDTO, DraftPost.class);
                    draftPost.setId(null);
                    draftPost.setPublishedPost(publishedPostOptional.get());
                    draftPost.setCategories(categories);
                    draftPost.setTags(tags);

                    if (draftPost.getPermalink() == null || draftPost.getPermalink().length() == 0) {
                        draftPost.setPermalink(SlugUtil.toSlug(draftPost.getTitle()));
                    }

                    draftPost = draftPostRepository.save(draftPost);

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

                    if (publishedPost.getPermalink() == null || publishedPost.getPermalink().length() == 0) {
                        publishedPost.setPermalink(SlugUtil.toSlug(publishedPost.getTitle()));
                    }

                    publishedPost = publishedPostRepository.save(publishedPost);
                    draftPostRepository.deleteById(postDTO.getId());

                    return new PostIdentityDTO(publishedPost.getId(), PostStatusDTO.PUBLISHED);

                } else if (postDTO.getStatus() == PostStatusDTO.PUBLISHED) {

                    Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(postDTO.getId());
                    if (!publishedPostOptional.isPresent()) {
                        // TODO: Throw exception
                    }

                    PublishedPost publishedPost = publishedPostOptional.get();
                    modelMapper.map(postDTO, publishedPost);
                    publishedPost.setPublishDate(dateUtility.getCurrentDate());
                    publishedPost.setCategories(categories);
                    publishedPost.setTags(tags);

                    if (publishedPost.getPermalink() == null || publishedPost.getPermalink().length() == 0) {
                        publishedPost.setPermalink(SlugUtil.toSlug(publishedPost.getTitle()));
                    }

                    publishedPost = publishedPostRepository.save(publishedPost);

                    return new PostIdentityDTO(publishedPost.getId(), PostStatusDTO.PUBLISHED);

                } else {
                    // TODO: Throw exception
                }

            }

        }

        // TODO: Add custom exception
        throw new IllegalStateException("Invalid post creation state.");

    }

    public PostDTO getPost(Long id, PostStatusDTO status) {

        if (status == PostStatusDTO.DRAFT) {
            Optional<DraftPost> draftPostOptional = draftPostRepository.findById(id);
            if (draftPostOptional.isPresent()) {
                DraftPost draftPost = draftPostOptional.get();
                return modelMapper.map(draftPost, PostDTO.class);
            }
        } else {
            Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(id);
            if (publishedPostOptional.isPresent()) {
                PublishedPost publishedPost = publishedPostOptional.get();
                return modelMapper.map(publishedPost, PostDTO.class);
            }
        }

        // TODO: Add custom exception
        throw new IllegalStateException("Could not find the post.");
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

    public Page<PostListDTO> getPosts(Pageable pageable) {

        List<PostListDTO> postListDTOList = new ArrayList<>();
        Page<Map<String, Object>> postPage = postRepository.getPostListAsAdmin(pageable);
        List<Map<String, Object>> posts = postPage.getContent();

        for (Map<String, Object> post : posts) {
            postListDTOList.add(new PostListDTO(
                    post.get("id").toString(),
                    (String) post.get("title"),
                    (String) post.get("status"),
                    (post.get("updated_at")).toString()
            ));
        }

        return new PageImpl<>(postListDTOList, pageable, postPage.getTotalElements());

    }

    public Page<PostDTO> getPublishedPosts(Pageable pageable) {

        Page<PublishedPost> postsPage = publishedPostRepository.findAll(pageable);
        List<PostDTO> postDTOList = postsPage.getContent().stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toCollection(ArrayList::new));

        return new PageImpl<>(postDTOList, pageable, postsPage.getTotalElements());
    }
}
