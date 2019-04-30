package com.vasiliska.MDBLibrary.service;

import com.vasiliska.MDBLibrary.domain.Book;
import com.vasiliska.MDBLibrary.repository.BookRep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ShellServiceImplTest {

    private final String TEST_BOOK_NAME = "Айвенго";
    private final String TEST_AUTHOR = "В.Скотт";
    private final String TEST_GENRE = "Роман";

    private final String TEST_BOOK_NAME2 = "Я вижу: человек сидит на стуле и стул кусает его за ногу";
    private final String TEST_AUTHOR2 = "Р.Шекли";
    private final String TEST_GENRE2 = "Фантастика";

    private final String TEST_COMMENT = "Книга супер!";


    @Autowired
    private BookRep bookRep;


    @Test
    public void addNewBook() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        insertTestBook(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);
        List<Book> books = bookRep.findAll();
        assertThat(books).hasSize(2);
    }

    @Test
    public void bookByName() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        Book book = bookRep.findBookByBookName(TEST_BOOK_NAME);
        assertTrue(book.toString().contains(TEST_BOOK_NAME));
    }


    @Test
    public void bookByGenre() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        List<Book> books = bookRep.findBookByGenre(TEST_GENRE);
        assertTrue(books.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void bookByAuthor() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        List<Book> books = bookRep.findBooksByAuthor(TEST_AUTHOR);
        assertTrue(books.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void showAllBooks() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        insertTestBook(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);

        List<Book> books = bookRep.findAll();
        assertThat(books).hasSize(2);
    }

    @Test
    public void addComment() {
        Book book = new Book(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        book.setComments(Arrays.asList(TEST_COMMENT));
        insertTestBook(book);

        book = bookRep.findBookByBookName(TEST_BOOK_NAME);
        assertThat(book.getComments()).contains(TEST_COMMENT);
    }


    @Test
    public void getBooksIsComment() {
        insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        insertTestBook(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);
        bookRep.deleteBookByBookName(TEST_BOOK_NAME);
        bookRep.deleteBookByBookName(TEST_BOOK_NAME2);

        Book book = new Book(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);
        book.setComments(Arrays.asList(TEST_COMMENT));
        insertTestBook(book);

        book = new Book(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        insertTestBook(book);

        List<Book> books = bookRep.findAll();
        assertThat(books).hasSize(2);

        books = bookRep.findBooksBy();
        assertThat(books).hasSize(1);
    }

    @Test
    public void delBook() {
        Book book = insertTestBook(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        int countBook0 = bookRep.findAll().size();

        bookRep.deleteBookByBookName(book.getBookName());
        int countBook1 = bookRep.findAll().size();
        assertThat((countBook0-countBook1)==1);
    }


    private Book insertTestBook(String bookName, String authorName, String genreName) {
        Book book = new Book(bookName, authorName, genreName);
        if (bookRep.findBookByBookName(bookName) != null) {
            return book;
        }
        bookRep.save(book);
        return book;
    }

    private Book insertTestBook(Book book) {
        if (bookRep.findBookByBookName(book.getBookName()) != null) {
            bookRep.deleteBookByBookName(book.getBookName());
        }
        bookRep.save(book);
        return book;
    }

}