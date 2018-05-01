package com.unloadbrain.blog.domain.repository;

import com.unloadbrain.blog.domain.model.PublishedPost;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishedPostRepository extends PagingAndSortingRepository<PublishedPost, Long> {

}
