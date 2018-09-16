package com.unloadbrain.blog.service;

import com.unloadbrain.blog.domain.model.Tag;
import com.unloadbrain.blog.domain.repository.TagRepository;
import com.unloadbrain.blog.dto.IdentityDTO;
import com.unloadbrain.blog.dto.TagDTO;
import com.unloadbrain.blog.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private static final String COMMA = ",";
    private static final String CACHE_KEY_TAGS = "tags";

    private TagRepository tagRepository;

    @Override
    public IdentityDTO createUpdateTag(TagDTO tagDTO) {

        Tag tag = null;

        if (tagDTO.getId() != null) {
            tag = tagRepository.getOne(tagDTO.getId());
        } else if (tagDTO.getName() != null) {
            tag = tagRepository.findByName(tagDTO.getName());
        }

        if(tag == null) {
            tag = new Tag();
        }

        tag.setName(tagDTO.getName());

        if (tagDTO.getSlug() == null || tagDTO.getSlug().trim().length() == 0) {
            tag.setSlug(SlugUtil.toSlug(tagDTO.getName()));
        } else {
            tag.setSlug(tagDTO.getSlug());
        }

        tag = tagRepository.save(tag);

        return new IdentityDTO(tag.getId());
    }

    @Override
    public void createUnsavedTags(String tags) {

        if (tags == null || tags.trim().length() == 0) {
            return;
        }

        for (String tagString : tags.split(COMMA)) {

            Optional<Tag> tagOptional = getTag(tagString);
            if (tagOptional.isPresent()) {
                continue;
            }

            TagDTO tagDTO = new TagDTO();
            tagDTO.setName(tagString);
            createUpdateTag(tagDTO);
        }
    }

    @Override
    public Optional<Tag> getTag(String tag) {
        Tag persistedTag = tagRepository.findByName(tag);
        return persistedTag != null ? Optional.of(persistedTag) : Optional.empty();
    }

    // FIXME:: Fix getTags first and then uncomment cache, don't forget to write tests.
    @Override
    //@Cacheable(CACHE_KEY_TAGS)
    public List<TagDTO> getTags() {
        return tagRepository.findAll().stream().map(this::convertToTagDTO).collect(Collectors.toList());
    }

    @Override
    public Set<Tag> getExistedTags(String tags) {

        Set<Tag> savedTags =  new LinkedHashSet<>();

        if (tags == null || tags.trim().length() == 0) {
            return savedTags;
        }

        for (String tagString : tags.split(COMMA)) {
            Optional<Tag> tagOptional = getTag(tagString);
            if (tagOptional.isPresent()) {
                savedTags.add(tagOptional.get());
            }
        }

        return savedTags;
    }

    private TagDTO convertToTagDTO(final Tag tag) {
        return new TagDTO(tag.getId(), tag.getName(), tag.getSlug());
    }
}
