package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.SiteQueryDTO;
import com.unloadbrain.blog.dto.SiteUpdateCommandDTO;

public interface SiteService {

    SiteQueryDTO getSite();

    void updateSite(SiteUpdateCommandDTO siteUpdateCommandDTO);
}
