package com.unloadbrain.blog.domain.repository;

import com.unloadbrain.blog.domain.model.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

}
