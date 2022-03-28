package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.model.entity.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static utils.TestTagFactory.createTag1;
import static utils.TestTagFactory.createTagRequestDto1;

class TagMapperTest {

    private final TagMapper tagMapper = new TagMapper(new ModelMapper());

    @Test
    void convertTagRequestDtoToTag_correctWork() {
        TagRequestDto tagRequestDto = createTagRequestDto1();
        Tag tag = tagMapper.convertTagRequestDtoToTag(tagRequestDto);
        assertNull(tag.getId());
        assertEquals(tagRequestDto.getName(), tag.getName());
    }

    @Test
    void convertTagToTagResponseDto_correctWork() {
        Tag tag = createTag1();
        TagResponseDto tagResponseDto = tagMapper.convertTagToTagResponseDto(tag);
        assertEquals(tag.getId(), tagResponseDto.getId());
        assertEquals(tag.getName(), tagResponseDto.getName());
    }
}