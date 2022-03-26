package utils;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.model.entity.Book;

import java.util.ArrayList;
import java.util.List;

import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME1;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME2;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID3;
import static utils.LibraryTestConstants.PRICE_BIG_DECIMAL1;
import static utils.LibraryTestConstants.PRICE_BIG_DECIMAL2;
import static utils.LibraryTestConstants.TITLE1;
import static utils.LibraryTestConstants.TITLE2;
import static utils.LibraryTestConstants.URL1;
import static utils.LibraryTestConstants.URL2;
import static utils.TestAuthorFactory.createAuthor1;
import static utils.TestAuthorFactory.createAuthor2;
import static utils.TestAuthorFactory.createAuthorRequestDto1;
import static utils.TestAuthorFactory.createAuthorRequestDto2;
import static utils.TestTagFactory.createTagSet1;
import static utils.TestTagFactory.createTagSet2;
import static utils.TestTagFactory.createTagsRequestDto;
import static utils.TestTagFactory.createTagsRequestDto2;

public class TestBookFactory {
    public static Book createBook1() {
        Book book = new Book();
        book.setId(ID1);
        book.setTitle(TITLE1);
        book.setImageUrl(URL1);
        book.setPrice(PRICE_BIG_DECIMAL1);
        book.setAuthor(createAuthor1());
        book.setTags(createTagSet1());
        book.setCreatedAt(DATE_ZONED_DATA_TIME1);
        book.setUpdatedAt(DATE_ZONED_DATA_TIME1);
        return book;
    }

    public static Book createBook2() {
        Book book = new Book();
        book.setId(ID3);
        book.setTitle(TITLE2);
        book.setImageUrl(URL2);
        book.setPrice(PRICE_BIG_DECIMAL2);
        book.setAuthor(createAuthor2());
        book.setTags(createTagSet2());
        book.setCreatedAt(DATE_ZONED_DATA_TIME2);
        book.setUpdatedAt(DATE_ZONED_DATA_TIME2);
        return book;
    }

    public static List<Book> createBookList1() {
        List<Book> books = new ArrayList<>();
        books.add(createBook1());
        books.add(createBook2());
        return books;
    }

    public static List<Book> createBookList2() {
        List<Book> books = new ArrayList<>();
        books.add(createBook1());
        return books;
    }

    public static BookRequestDto createBookRequestDto1() {
        return new BookRequestDto(TITLE1, URL1, PRICE_BIG_DECIMAL1, createTagsRequestDto(), createAuthorRequestDto1());
    }

    public static BookRequestDto createBookRequestDto2() {
        return new BookRequestDto(TITLE2, URL2, PRICE_BIG_DECIMAL2, createTagsRequestDto2(), createAuthorRequestDto2());
    }
}


