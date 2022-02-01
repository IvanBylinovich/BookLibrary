package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping("/allTags")
    public ResponseEntity<Set<TagResponseDto>> getAllTags() {
        return ResponseEntity.ok().body(tagService.findAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDto> getTagById(@PathVariable String id) {
        return ResponseEntity.ok().body(tagService.findTagById(id));
    }

    @PostMapping()
    public ResponseEntity<TagResponseDto> createTag(@RequestBody @Valid TagRequestDto tagRequestDto) {
        return ResponseEntity.ok().body(tagService.createTag(tagRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable String id, @RequestBody @Valid TagRequestDto tagRequestDto) {
        return ResponseEntity.ok().body(tagService.updateTag(id, tagRequestDto));
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable String id) {
        tagService.deleteTag(id);
    }
}
