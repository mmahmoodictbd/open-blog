package com.unloadbrain.blog.domain.repository;

import com.unloadbrain.blog.domain.model.DraftPost;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftPostRepository extends PagingAndSortingRepository<DraftPost, Long> {

}
