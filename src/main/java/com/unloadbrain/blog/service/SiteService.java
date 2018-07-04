package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Site;
import com.unloadbrain.blog.domain.repository.SiteRepository;
import com.unloadbrain.blog.dto.UpdateSiteRequest;
import com.unloadbrain.blog.exception.SiteNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SiteService {

    private SiteRepository siteRepository;

    public Site getSite() {

        Optional<Site> site = getFirstSite();

        throwExceptionWhenSiteNotFound(site);

        return site.get();

    }

    private void throwExceptionWhenSiteNotFound(Optional<Site> site) {
        if (!site.isPresent()) {
            throw new SiteNotFoundException("Site information should be pre persisted using init data mechanism.");
        }
    }

    public void updateSite(UpdateSiteRequest updateSiteRequest) {

        Optional<Site> siteOptional = getFirstSite();

        throwExceptionWhenSiteNotFound(siteOptional);

        // TODO: use mapper
        Site site = siteOptional.get();
        site.setName(updateSiteRequest.getName());
        site.setDescription(updateSiteRequest.getDescription());
        site.setHomeUrl(updateSiteRequest.getHomeUrl());
        site.setSiteUrl(updateSiteRequest.getSiteUrl());
        site.setAdditionalProperties(updateSiteRequest.getAdditionalProperties());

        siteRepository.save(site);
    }

    private Optional<Site> getFirstSite() {

        List<Site> sites = siteRepository.findAll();

        if (sites.size() > 0) {
            return Optional.of(sites.get(0));
        }

        return Optional.empty();
    }

}
