package com.vasiliska.MDBLibrary.shell;

import com.vasiliska.MDBLibrary.domain.Book;
import com.vasiliska.MDBLibrary.repository.BookRep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandsTest {

    @Autowired
    private Shell shell;

    @Autowired
    private BookRep bookRep;

    private final String TEST_BOOK_NAME1 = "Aйвенго";
    private final String TEST_BOOK_AUTHOR1 = "В.Скотт";
    private final String TEST_BOOK_GENRE1 = "Роман";

    private final String TEST_BOOK_NAME2 = "Я вижу: человек сидит на стуле и стул кусает его за ногу";
    private final String TEST_BOOK_AUTHOR2 = "Р.Шекли";
    private final String TEST_BOOK_GENRE2 = "Фантастика";

    private final String TEST_COMMENT1 = "Жесть!";

    @Test
    public void findbook() {
        insertTestBook(TEST_BOOK_NAME1, TEST_BOOK_AUTHOR1, TEST_BOOK_GENRE1);
        Object textTest = shell.evaluate(() -> "findbook Aйвенго");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME1));
    }

    @Test
    public void addbook() {
        Object textTest = shell.evaluate(() -> "addbook Aйвенго В.Скотт Роман");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME1));
    }

    @Test
    public void delbook() {
        insertTestBook(TEST_BOOK_NAME1, TEST_BOOK_AUTHOR1, TEST_BOOK_GENRE1);
        Object textTest = shell.evaluate(() -> "delbook Aйвенго");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME1));
    }

    @Test
    public void allbooks() {
        insertTestBook(TEST_BOOK_NAME2, TEST_BOOK_AUTHOR2, TEST_BOOK_GENRE2);
        Object textTest = shell.evaluate(() -> "allbooks");

        assertTrue(textTest.toString().contains(TEST_BOOK_NAME2));
        assertTrue(textTest.toString().contains(TEST_BOOK_AUTHOR2));
        assertTrue(textTest.toString().contains(TEST_BOOK_GENRE2));
    }

    @Test
    public void byauthor() {
        insertTestBook(TEST_BOOK_NAME1, TEST_BOOK_AUTHOR1, TEST_BOOK_GENRE1);
        Object textTest = shell.evaluate(() -> "byauthor В.Скотт");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME1));
    }

    @Test
    public void bygenre() {
        insertTestBook(TEST_BOOK_NAME1, TEST_BOOK_AUTHOR1, TEST_BOOK_GENRE1);
        Object textTest = shell.evaluate(() -> "bygenre Роман");
        assertTrue(textTest.toString().contains(TEST_BOOK_NAME1));
    }

    @Test
    public void addcomment() {
        Book book = insertTestBook(TEST_BOOK_NAME1, TEST_BOOK_AUTHOR1, TEST_BOOK_GENRE1);
        shell.evaluate(() -> "addcomment " + TEST_COMMENT1 + " " + TEST_BOOK_NAME1);

        List<String> result = bookRep.findBookByBookName(TEST_BOOK_NAME1).getComments();
        assertThat(result).isNotNull().hasSize(1).toString().contains(TEST_COMMENT1);
    }

    private Book insertTestBook(String bookName, String authorName, String genreName) {
        Book book = new Book(bookName, authorName, genreName);
        if (bookRep.findBookByBookName(bookName) != null) {
            return book;
        }
        bookRep.save(book);
        return book;
    }

}