package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.service.AuthorService;
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
import java.util.List;

import static com.solbeg.BookLibrary.config.SwaggerConfig.AUTHOR_SERVICE_SWAGGER_TAG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
@Api(tags = {AUTHOR_SERVICE_SWAGGER_TAG})
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/allAuthors")
    @ApiOperation(value = "Get all authors", notes = "Provide method to get all authors", response = ResponseEntity.class)
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        return ResponseEntity.ok().body(authorService.findAllAuthors());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get author by id", notes = "Provide method to get author", response = ResponseEntity.class)
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable String id) {
        return ResponseEntity.ok().body(authorService.findAuthorById(id));
    }

    @PostMapping
    @ApiOperation(value = "Create author", notes = "Provide method to create author", response = ResponseEntity.class)
    public ResponseEntity<AuthorResponseDto> createAuthor(@RequestBody @Valid AuthorRequestDto authorRequestDto) {
        return ResponseEntity.ok().body(authorService.createAuthor(authorRequestDto));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update author", notes = "Provide method to update author", response = ResponseEntity.class)
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable String id, @RequestBody @Valid AuthorRequestDto authorRequestDto) {
        return ResponseEntity.ok().body(authorService.updateAuthor(id, authorRequestDto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete author by id", notes = "Provide method to delete author")
    public void deleteAuthor(@PathVariable String id) {
        authorService.deleteAuthor(id);
    }
}