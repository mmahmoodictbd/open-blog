package com.unloadbrain.blog.domain.repository;

import com.unloadbrain.blog.domain.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

}
