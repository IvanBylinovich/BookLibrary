package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.authorDto.AuthorSavingDTO;
import com.solbeg.BookLibrary.dto.authorDto.AuthorUpdateDTO;
import com.solbeg.BookLibrary.entity.Author;
import com.solbeg.BookLibrary.service.AuthorService;
import com.solbeg.BookLibrary.service.implementation.AuthorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {
    final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorServiceImpl authorServiceImpl) {
        this.authorService = authorServiceImpl;
    }

    @GetMapping
    public Author getAuthorById(long id) {
        return authorService.findAuthorById(id);
    }

    @PostMapping
    public Author createAuthor(@RequestBody AuthorSavingDTO dto) {
        return authorService.createAuthor(dto);
    }

    @GetMapping("/allAuthor")
    public List<Author> getAllAuthor() {
        return authorService.getAllAuthor();
    }

    @DeleteMapping
    public void deleteAuthor(long id) {
        authorService.deleteAuthor(id);
    }

    @PatchMapping
    public void updateAuthor(@RequestBody AuthorUpdateDTO dto) {
        authorService.updateAuthor(dto);
    }

}
