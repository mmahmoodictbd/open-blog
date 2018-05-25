package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class TagService {

    private static final String COMMA = ",";

    private TagRepository tagRepository;

    public Set<Tag> getTags(String tagsString) {

        Set<Tag> tags = new LinkedHashSet<>();

        if (tagsString == null || tagsString.trim().length() == 0) {
            return tags;
        }

        for (String tagString : tagsString.split(COMMA)) {
            Tag persistedTag = tagRepository.findByName(tagString);
            if (persistedTag == null) {
                Tag tag = new Tag();
                tag.setName(tagString);
                tag.setSlug(SlugUtil.toSlug(tagString));
                persistedTag = tagRepository.save(tag);
            }
            tags.add(persistedTag);
        }

        return tags;
    }

}
