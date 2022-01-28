package com.solbeg.BookLibrary.service;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.model.entity.Tag;

import java.util.Set;

public interface TagService {

    Set<TagResponseDto> findAllTags();

    TagResponseDto findTagById(String id);

    TagResponseDto createTag(TagRequestDto tagRequestDto);

    TagResponseDto updateTag(String id, TagRequestDto tagRequestDto);

    void deleteTag(String id);

    Set<Tag> getExistingTagsOrThrowException(Set<TagRequestDto> tags);
}
