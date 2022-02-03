package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.exception.TagAlreadyExistByNameException;
import com.solbeg.BookLibrary.exception.TagNotFoundByIdException;
import com.solbeg.BookLibrary.exception.TagNotFoundByNameException;
import com.solbeg.BookLibrary.mapper.TagMapper;
import com.solbeg.BookLibrary.model.entity.Tag;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.repository.TagRepository;
import com.solbeg.BookLibrary.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final BookRepository bookRepository;
    private final TagMapper tagMapper;

    @Override
    public Set<TagResponseDto> findAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::convertTagToTagResponseDto)
                .collect(Collectors.toSet());
    }

    @Override
    public TagResponseDto findTagById(String id) {
        return tagMapper.convertTagToTagResponseDto(findTagOrThrowException(id));
    }

    @Override
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {
        Tag tag = tagRepository.findTagByName(tagRequestDto.getName()).orElse(null);
        if (tag == null) {
            tag = tagMapper.convertTagRequestDtoToTag(tagRequestDto);
            tagRepository.save(tag);
        }
        return tagMapper.convertTagToTagResponseDto(tag);
    }

    @Override
    public TagResponseDto updateTag(String id, TagRequestDto tagRequestDto) {
        Tag existingTag = findTagOrThrowException(id);
        Tag tag = tagRepository.findTagByName(tagRequestDto.getName()).orElse(null);
        if (tag != null) {
            throw new TagAlreadyExistByNameException(tag.getName(), tag.getId());
        }
        existingTag.setName(tagRequestDto.getName());
        return tagMapper.convertTagToTagResponseDto(tagRepository.save(existingTag));
    }

    @Transactional
    @Override
    public void deleteTag(String id) {
        Tag tag = findTagOrThrowException(id);
        bookRepository.findAllByTagsContains(tag).ifPresent(books -> {
            books.forEach(book -> book.getTags().remove(tag));
            bookRepository.saveAll(books);
        });
        tagRepository.delete(tag);
    }

    public Set<Tag> getExistingTagsOrThrowException(Set<TagRequestDto> tags) {
        if (tags == null) {
            return new HashSet<>();
        }
        return tags.stream()
                .map(t -> tagRepository.findTagByName(t.getName())
                        .orElseThrow(() -> new TagNotFoundByNameException(t.getName())))
                .collect(Collectors.toSet());
    }

    private Tag findTagOrThrowException(String id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundByIdException(id));
    }
}