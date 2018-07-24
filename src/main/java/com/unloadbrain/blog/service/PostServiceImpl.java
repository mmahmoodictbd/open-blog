package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.DraftPost;
import com.unloadbrain.blog.domain.model.PublishedPost;
import com.unloadbrain.blog.domain.repository.DraftPostRepository;
import com.unloadbrain.blog.domain.repository.PostRepository;
import com.unloadbrain.blog.domain.repository.PublishedPostRepository;
import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostListDTO;
import com.unloadbrain.blog.dto.PostQueryDTO;
import com.unloadbrain.blog.exception.DraftPostNotFoundException;
import com.unloadbrain.blog.exception.PublishedPostNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private PublishedPostRepository publishedPostRepository;
    private DraftPostRepository draftPostRepository;

    private PostRepository postRepository;

    private ModelMapper modelMapper;


    @Override
    public PostQueryDTO getPost(Long id, CurrentPostStatus status) {

        if (status == CurrentPostStatus.DRAFT) {
            Optional<DraftPost> draftPostOptional = draftPostRepository.findById(id);
            if (draftPostOptional.isPresent()) {
                DraftPost draftPost = draftPostOptional.get();
                return modelMapper.map(draftPost, PostQueryDTO.class);
            } else {
                throw new DraftPostNotFoundException("Could not find the draft post.");
            }
        } else {
            Optional<PublishedPost> publishedPostOptional = publishedPostRepository.findById(id);
            if (publishedPostOptional.isPresent()) {
                PublishedPost publishedPost = publishedPostOptional.get();
                return modelMapper.map(publishedPost, PostQueryDTO.class);
            } else {
                throw new PublishedPostNotFoundException("Could not find the published post.");
            }
        }

    }


    @Override
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


}
