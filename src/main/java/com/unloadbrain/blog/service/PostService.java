package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Post;
import com.unloadbrain.blog.domain.repository.PostRepository;
import com.unloadbrain.blog.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostService(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public Post createPost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        postRepository.save(post);
        return post;
    }

}
