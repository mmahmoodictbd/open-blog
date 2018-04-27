package com.unloadbrain.blog.domain.repository;

import com.unloadbrain.blog.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select category  from Category category where category.name = ?1")
    Category findByName(String name);
}
