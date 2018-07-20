package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.dto.TagDTO;

import java.util.List;
import java.util.Set;

public interface TagService {

    Set<Tag> getTags(String tagsString);

    List<TagDTO> getTags();
}
