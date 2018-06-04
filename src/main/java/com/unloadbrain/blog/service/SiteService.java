package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Site;
import com.unloadbrain.blog.domain.repository.SiteRepository;
import com.unloadbrain.blog.exception.SiteNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SiteService {

    private SiteRepository siteRepository;

    public Site getSite() {

        List<Site> sites = siteRepository.findAll();

        if (sites.size() > 0) {
            return sites.get(0);
        }

        throw new SiteNotFoundException("Site information should be pre persisted using init data mechanism.");

    }

}
