package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.CurrentPostStatus;
import com.unloadbrain.blog.dto.PostListDTO;
import com.unloadbrain.blog.dto.PostQueryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostQueryDTO getPost(Long id, CurrentPostStatus status);

    Page<PostListDTO> getPosts(Pageable pageable);
}
