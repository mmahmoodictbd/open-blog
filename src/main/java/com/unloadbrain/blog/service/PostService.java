package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.repository.PostRepository;
import org.springframework.stereotype.Service;


@Service
public class PostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

}
