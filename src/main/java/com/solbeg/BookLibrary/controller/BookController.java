package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.service.BookService;
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

import static com.solbeg.BookLibrary.config.SwaggerConfig.BOOK_SERVICE_SWAGGER_TAG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Api(tags = {BOOK_SERVICE_SWAGGER_TAG})
public class BookController {

    private final BookService bookService;

    @GetMapping("/allBooks")
    @ApiOperation(value = "Get all books", notes = "Provide method to get all books", response = ResponseEntity.class)
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok().body(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get book by id", notes = "Provide method to get book", response = ResponseEntity.class)
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable String id) {
        return ResponseEntity.ok().body(bookService.findBookById(id));
    }

    @PostMapping
    @ApiOperation(value = "Create book", notes = "Provide method to create book", response = ResponseEntity.class)
    public ResponseEntity<BookResponseDto> createBook(@RequestBody @Valid BookRequestDto bookRequestDto) {
        return ResponseEntity.ok().body(bookService.createBook(bookRequestDto));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update book", notes = "Provide method to update book", response = ResponseEntity.class)
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable String id, @RequestBody @Valid BookRequestDto bookRequestDto) {
        return ResponseEntity.ok().body(bookService.updateBook(id, bookRequestDto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete book", notes = "Provide method to delete book")
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }
}