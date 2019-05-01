package com.vasiliska.MDBLibrary.service;

import com.vasiliska.MDBLibrary.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ShellServiceImplTest {

    private final String TEST_BOOK_NAME = "Айвенго";
    private final String TEST_AUTHOR = "В.Скотт";
    private final String TEST_GENRE = "Роман";

    private final String TEST_BOOK_NAME2 = "Я вижу: человек сидит на стуле и стул кусает его за ногу";
    private final String TEST_AUTHOR2 = "Р.Шекли";
    private final String TEST_GENRE2 = "Фантастика";

    private final String TEST_COMMENT = "Книга супер!";

    @Autowired
    private ShellServiceImpl shellService;


    @Test
    public void addNewBook() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        insertTestBook(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);
        String books = shellService.showAllBooks();
        assertThat(books).contains(TEST_BOOK_NAME);
    }

    @Test
    public void bookByName() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        String name = shellService.bookByName(TEST_BOOK_NAME);
        assertTrue(name.contains(TEST_BOOK_NAME));
    }


    @Test
    public void bookByGenre() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        String books = shellService.bookByGenre(TEST_GENRE);
        assertTrue(books.contains(TEST_BOOK_NAME));
    }

    @Test
    public void bookByAuthor() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        String books = shellService.bookByAuthor(TEST_AUTHOR);
        assertTrue(books.contains(TEST_BOOK_NAME));
    }

    @Test
    public void showAllBooks() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        insertTestBook(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);
        String books = shellService.showAllBooks();
        assertThat(books).contains(TEST_BOOK_NAME).contains(TEST_BOOK_NAME2);
    }

    @Test
    public void addComment() {
        shellService.addComment(TEST_COMMENT, TEST_BOOK_NAME);
        String comment = shellService.getCommentsByBook(TEST_BOOK_NAME);
        assertThat(comment).contains(TEST_COMMENT);
    }


    @Test
    public void delBook() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        shellService.delBook(TEST_BOOK_NAME);
        String name = shellService.bookByName(TEST_BOOK_NAME);
        assertFalse(name.contains(TEST_BOOK_NAME));
    }


    private Book insertTestBook(String bookName, String authorName, String genreName) {
        Book book = new Book(bookName, authorName, genreName);
        shellService.addNewBook(bookName, authorName, genreName);
        return book;
    }


}