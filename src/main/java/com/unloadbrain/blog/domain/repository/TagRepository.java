package com.unloadbrain.blog.domain.repository;

import com.unloadbrain.blog.domain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select tag  from Tag tag where tag.name = ?1")
    Tag findByName(String name);
}
