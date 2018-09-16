package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.dto.IdentityDTO;
import com.unloadbrain.blog.dto.TagDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService {

    IdentityDTO createUpdateTag(TagDTO tagDTO);

    void createUnsavedTags(String tags);

    Optional<Tag> getTag(String tag);

    List<TagDTO> getTags();

    Set<Tag> getExistedTags(String tags);
}
