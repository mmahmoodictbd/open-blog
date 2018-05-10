package com.unloadbrain.blog.domain.repository;

import com.unloadbrain.blog.domain.model.PublishedPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PublishedPost, Long> {

    String FETCH_POST_LIST_FOR_ADMIN =
            "SELECT pp.id, pp.title, 'PUBLISHED' status, pp.updated_at "
                    + "FROM published_post pp "
                    + "LEFT JOIN draft_post dp ON(dp.published_post_id = pp.id) "
                    + "WHERE dp.id IS NULL "
                    + "UNION "
                    + "SELECT dp.id, dp.title, 'DRAFT' status, dp.updated_at "
                    + "FROM draft_post dp "
                    + "ORDER BY updated_at ";

    String COUNT_FETCH_POST_LIST_FOR_ADMIN =
            "SELECT SUM(p.c) c1 FROM ("
                    + "  SELECT COUNT(pp.id) c FROM published_post pp "
                    + "  LEFT JOIN draft_post dp ON(dp.published_post_id = pp.id) "
                    + "  WHERE dp.id IS NULL "
                    + "  UNION "
                    + "  SELECT COUNT(dp.id) c FROM draft_post dp "
                    + ") p ";


    @Query(name = "getPostListAsAdmin",
            value = FETCH_POST_LIST_FOR_ADMIN,
            countQuery = COUNT_FETCH_POST_LIST_FOR_ADMIN,
            nativeQuery = true)
    Page<Map<String, Object>> getPostListAsAdmin(Pageable pageable);
}
