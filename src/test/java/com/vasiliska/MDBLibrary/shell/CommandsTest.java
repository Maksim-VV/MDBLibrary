package com.vasiliska.MDBLibrary.shell;

import com.vasiliska.MDBLibrary.domain.Book;
import com.vasiliska.MDBLibrary.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CommandsTest {


    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private Shell shell;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    Commands commands;

    private final String TEST_BOOK_NAME = "Aйвенго";
    private final String TEST_AUTHOR = "В.Скотт";
    private final String TEST_GENRE = "Роман";

    private final String TEST_BOOK_NAME2 = "Я вижу: человек сидит на стуле и стул кусает его за ногу";
    private final String TEST_AUTHOR2 = "Р.Шекли";
    private final String TEST_GENRE2 = "Фантастика";

    private final String TEST_COMMENT1 = "Жесть!";

    @Before
    public void setUp() throws Exception {
        mongoTemplate.getDb().drop();
        Book book = new Book(TEST_BOOK_NAME, TEST_AUTHOR, TEST_GENRE);
        bookRepository.save(book);
        book = new Book(TEST_BOOK_NAME2, TEST_AUTHOR2, TEST_GENRE2);
        book.setComments(Arrays.asList(TEST_COMMENT1));
        bookRepository.save(book);
    }

    @Test
    public void findbook() {
        Object textTest = shell.evaluate(() -> "findbook Aйвенго");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void addbook() {
        Object textTest = shell.evaluate(() -> "addbook Aйвенго В.Скотт Роман");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void delbook() {
        Object textTest = shell.evaluate(() -> "delbook Aйвенго");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void allbooks() {
        Object textTest = shell.evaluate(() -> "allbooks");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME2));
        assertTrue(textTest.toString().contains(TEST_AUTHOR2));
        assertTrue(textTest.toString().contains(TEST_GENRE2));
    }

    @Test
    public void byauthor() {
        Object textTest = shell.evaluate(() -> "byauthor В.Скотт");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME));
    }

    @Test
    public void bygenre() {
        Object textTest = shell.evaluate(() -> "bygenre Фантастика");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME2));
    }

    @Test
    public void addcomment() {
        shell.evaluate(() -> "addcomment " + TEST_COMMENT1 + " " + TEST_BOOK_NAME);
        List<String> result = bookRepository.findBookByBookName(TEST_BOOK_NAME).getComments();
        assertThat(result).isNotNull().hasSize(1).toString().contains(TEST_COMMENT1);
    }


}