package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Site;
import com.unloadbrain.blog.domain.repository.SiteRepository;
import com.unloadbrain.blog.dto.SiteQueryDTO;
import com.unloadbrain.blog.dto.SiteUpdateCommandDTO;
import com.unloadbrain.blog.exception.SiteNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SiteService {

    private SiteRepository siteRepository;

    private ModelMapper modelMapper;

    public SiteQueryDTO getSite() {

        Optional<Site> site = getFirstSite();

        throwExceptionWhenSiteNotFound(site);

        return modelMapper.map(site.get(), SiteQueryDTO.class);

    }

    private void throwExceptionWhenSiteNotFound(Optional<Site> site) {
        if (!site.isPresent()) {
            throw new SiteNotFoundException("Site information should be pre persisted using init data mechanism.");
        }
    }

    public void updateSite(SiteUpdateCommandDTO siteUpdateCommandDTO) {

        Optional<Site> siteOptional = getFirstSite();

        throwExceptionWhenSiteNotFound(siteOptional);

        Site site = siteOptional.get();
        modelMapper.map(siteUpdateCommandDTO, site);

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
