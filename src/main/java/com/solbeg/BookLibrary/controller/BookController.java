package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.bookDto.BookSavingDTO;
import com.solbeg.BookLibrary.dto.bookDto.BookUpdateDTO;
import com.solbeg.BookLibrary.entity.Book;
import com.solbeg.BookLibrary.service.implementation.BookServiceImpl;
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
@RequestMapping("/book")
public class BookController {

    final
    BookServiceImpl bookServiceImpl;

    @Autowired
    public BookController(BookServiceImpl bookServiceImpl) {
        this.bookServiceImpl = bookServiceImpl;
    }

    @GetMapping
    public Book getBook(long id) {
        return bookServiceImpl.getBookById(id);
    }

    @PostMapping
    public Book createBook(@RequestBody BookSavingDTO dto) {
        return bookServiceImpl.createBook(dto);
    }

    @DeleteMapping("/delete")
    public void deleteBook(long id) {
        bookServiceImpl.deleteBook(id);

    }

    @PatchMapping
    public void updateBook(@RequestBody BookUpdateDTO dto) {
        bookServiceImpl.updateBook(dto);
    }

    @GetMapping("/getAll")
    public List<Book> getAllBook() {
        return bookServiceImpl.getAllBook();
    }
}
