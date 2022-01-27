package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.service.BookService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/allBooks")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok().body(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable String id) {
        return ResponseEntity.ok().body(bookService.findBookById(id));
    }

    @PostMapping()
    public ResponseEntity<BookResponseDto> createBook(@RequestBody @Valid BookRequestDto bookRequestDto) {
        return ResponseEntity.ok().body(bookService.createBook(bookRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable String id, @RequestBody @Valid BookRequestDto bookRequestDto) {
        return ResponseEntity.ok().body(bookService.updateBook(id, bookRequestDto));
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }
}