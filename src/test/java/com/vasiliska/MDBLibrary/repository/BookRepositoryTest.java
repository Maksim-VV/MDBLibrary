package com.vasiliska.MDBLibrary.repository;

import com.vasiliska.MDBLibrary.domain.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookRepositoryTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository bookRepository;

    private final String TEST_BOOK_NAME = "Aйвенго";
    private final String TEST_AUTHOR = "В.Скотт";
    private final String TEST_GENRE = "Роман";

    private final String TEST_BOOK_NAME2 = "Я вижу: человек сидит на стуле и стул кусает его за ногу";
    private final String TEST_AUTHOR2 = "Р.Шекли";
    private final String TEST_GENRE2 = "Фантастика";

    @Before
    public void setUp() throws Exception {
        mongoTemplate.getDb().drop();
        Book book = new Book(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        bookRepository.save(book);
        book = new Book(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);
        bookRepository.save(book);
    }


    @Test
    public void findBookByBookName() {
        Book book = bookRepository.findBookByBookName(TEST_BOOK_NAME);
        assertTrue(book.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void findBooksByAuthor() {
        List<Book> books = bookRepository.findBooksByAuthor(TEST_AUTHOR);
        assertTrue(books.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void findBookByGenre() {
        List<Book> books = bookRepository.findBookByGenre(TEST_GENRE);
        assertTrue(books.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void deleteBookByBookName() {
        int countBook0 = bookRepository.findAll().size();
        bookRepository.deleteBookByBookName(TEST_BOOK_NAME);
        int countBook1 = bookRepository.findAll().size();
        assertThat((countBook0 - countBook1) == 1);
    }


}