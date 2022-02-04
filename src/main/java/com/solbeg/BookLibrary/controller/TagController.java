package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import static com.solbeg.BookLibrary.config.SwaggerConfig.TAG_SERVICE_SWAGGER_TAG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
@Api(tags = {TAG_SERVICE_SWAGGER_TAG})
public class TagController {

    private final TagService tagService;

    @GetMapping("/allTags")
    @ApiOperation(value = "Get all tags", notes = "Provide method to get all tags", response = ResponseEntity.class)
    public ResponseEntity<Set<TagResponseDto>> getAllTags() {
        return ResponseEntity.ok().body(tagService.findAllTags());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get tag by id", notes = "Provide method to get tag", response = ResponseEntity.class)
    public ResponseEntity<TagResponseDto> getTagById(@PathVariable String id) {
        return ResponseEntity.ok().body(tagService.findTagById(id));
    }

    @PostMapping
    @ApiOperation(value = "Create tag", notes = "Provide method to create tag", response = ResponseEntity.class)
    public ResponseEntity<TagResponseDto> createTag(@RequestBody @Valid TagRequestDto tagRequestDto) {
        return ResponseEntity.ok().body(tagService.createTag(tagRequestDto));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update tag", notes = "Provide method to update tag", response = ResponseEntity.class)
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable String id, @RequestBody @Valid TagRequestDto tagRequestDto) {
        return ResponseEntity.ok().body(tagService.updateTag(id, tagRequestDto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete tag", notes = "Provide method to delete tag")
    public void deleteTag(@PathVariable String id) {
        tagService.deleteTag(id);
    }
}
