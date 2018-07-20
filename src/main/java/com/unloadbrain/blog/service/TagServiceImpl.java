package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.dto.TagDTO;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private static final String COMMA = ",";
    private static final String CACHE_KEY_TAGS = "tags";

    private TagRepository tagRepository;

    @Override
    public Set<Tag> getTags(String tagsString) {

        Set<Tag> tags = new LinkedHashSet<>();

        if (tagsString == null || tagsString.trim().length() == 0) {
            return tags;
        }

        // FIXME:: Bad design, violating command query separation

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


    // FIXME:: Fix getTags first and then uncomment cache, don't forget to write tests.
    @Override
    //@Cacheable(CACHE_KEY_TAGS)
    public List<TagDTO> getTags() {
        return tagRepository.findAll().stream().map(this::convertToTagDTO).collect(Collectors.toList());
    }

    private TagDTO convertToTagDTO(final Tag tag) {
        return new TagDTO(tag.getId(), tag.getName(), tag.getSlug());
    }
}
