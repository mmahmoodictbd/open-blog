package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.PostDTO;
import com.unloadbrain.blog.dto.PostIdentityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublishPostService {

    PostIdentityDTO createUpdatePost(PostDTO postDTO);

    PostDTO getPost(Long id);

    String getPermalink(Long postId);

    Page<PostDTO> getPosts(Pageable pageable);
}
