package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.model.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagMapper {

    private final ModelMapper modelMapper;

    public Tag convertTagRequestDtoToTag(TagRequestDto tagRequestDto) {
        return modelMapper.map(tagRequestDto, Tag.class);
    }

    public TagResponseDto convertTagToTagResponseDto(Tag tag) {
        return modelMapper.map(tag, TagResponseDto.class);
    }
}
