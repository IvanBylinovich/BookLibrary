package utils;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.model.entity.Author;

import java.util.ArrayList;
import java.util.List;

import static utils.LibraryTestConstants.FIRST_NAME1;
import static utils.LibraryTestConstants.FIRST_NAME2;
import static utils.LibraryTestConstants.FIRST_NAME3;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.LAST_NAME1;
import static utils.LibraryTestConstants.LAST_NAME2;
import static utils.LibraryTestConstants.LAST_NAME3;

public class TestAuthorFactory {

    public static Author createAuthor1() {
        Author author = new Author();
        author.setId(ID1);
        author.setFirstName(FIRST_NAME1);
        author.setLastName(LAST_NAME1);
        return author;
    }

    public static Author createAuthor2() {
        Author author = new Author();
        author.setId(ID2);
        author.setFirstName(FIRST_NAME2);
        author.setLastName(LAST_NAME2);
        return author;
    }

    public static Author createAuthor1_notSaved() {
        Author author = new Author();
        author.setFirstName(FIRST_NAME1);
        author.setLastName(LAST_NAME1);
        return author;
    }

    public static Author createAuthor2_notSaved() {
        Author author = new Author();
        author.setFirstName(FIRST_NAME2);
        author.setLastName(LAST_NAME2);
        return author;
    }

    public static List<Author> createAuthorList() {
        List<Author> authors = new ArrayList<>();
        authors.add(createAuthor1());
        authors.add(createAuthor2());
        return authors;
    }

    public static AuthorRequestDto createAuthorRequestDto1() {
        return new AuthorRequestDto(FIRST_NAME1, LAST_NAME1);
    }

    public static AuthorRequestDto createAuthorRequestDto2() {
        return new AuthorRequestDto(FIRST_NAME2, LAST_NAME2);
    }

    public static AuthorRequestDto createAuthorRequestDto3() {
        return new AuthorRequestDto(FIRST_NAME3, LAST_NAME3);
    }
}


