package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.service.AuthorService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/allAuthor")
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        return ResponseEntity.ok().body(authorService.findAllAuthors());
    }

    @GetMapping
    public ResponseEntity<AuthorResponseDto> getAuthorById(String id) {
        return ResponseEntity.ok().body(authorService.findAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> createAuthor(@RequestBody AuthorRequestDto authorRequestDto) {
        return ResponseEntity.ok().body(authorService.createAuthor(authorRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable String id, @RequestBody AuthorRequestDto authorRequestDto) {
        return ResponseEntity.ok().body(authorService.updateAuthor(id, authorRequestDto));
    }

    @DeleteMapping
    public void deleteAuthor(String id) {
        authorService.deleteAuthor(id);
    }
}
