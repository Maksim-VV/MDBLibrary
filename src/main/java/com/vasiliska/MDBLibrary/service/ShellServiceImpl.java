package com.vasiliska.MDBLibrary.service;


import com.vasiliska.MDBLibrary.domain.Book;
import com.vasiliska.MDBLibrary.repository.BookRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShellServiceImpl implements ShellService {

    private BookRep bookRep;

    @Autowired
    public ShellServiceImpl(BookRep bookRep) {
        this.bookRep = bookRep;
    }

    private final String MSG_DONT_FIND = "Объект не найден!";
    private final String MSG_ADD_NEW_BOOK = "Книга \"%s\" добавлена.";
    private final String MSG_ADD_NEW_COMMENT = "Комментарий на книгу \"%s\" добавлен.";
    private final String MSG_DELETE_BOOK = "Книга \"%s\" удалена из библиотеки.";
    private final String MSG_BOOK_IS_EXIST = "Книга \"%s\" уже есть в библиотеке.";
    private final String MSG_SHOW_COMMENT = "Комментарии на книгу \"%s\":\n";
    private final String MSG_SHOW_BOOKS_COMMENT = "Книги на которые есть комментарии:\n";

    @Override
    public String addNewBook(String bookName, String authorName, String genreName) {
        Book book = bookRep.findBookByBookName(bookName);
        if (book != null) {
            if (book.getAuthor().equals(authorName) && book.getGenre().equals(genreName)) {
                return String.format(MSG_BOOK_IS_EXIST, bookName);
            }
        }

        bookRep.save(new Book(bookName, authorName, genreName));
        return String.format(MSG_ADD_NEW_BOOK, bookName);
    }

    @Override
    public String bookByName(String bookName) {
        return bookRep.findBookByBookName(bookName).toString();
    }

    @Override
    public String delBook(String bookName) {
        if (bookRep.deleteBookByBookName(bookName) > 0) {
            return String.format(MSG_DELETE_BOOK, bookName);
        }
        return String.format(MSG_DONT_FIND, bookName);
    }

    @Override
    public String bookByGenre(String genre) {
        return showBooks(bookRep.findBookByGenre(genre));
    }

    @Override
    public String bookByAuthor(String author) {
        return showBooks(bookRep.findBooksByAuthor(author));
    }

    @Override
    public String showAllBooks() {
        return showBooks(bookRep.findAll());
    }

    @Override
    public String addComment(String commentText, String bookName) {
        Book book = bookRep.findBookByBookName(bookName);
        if (book == null) {
            return MSG_DONT_FIND;
        }

        List<String> comments = book.getComments();
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(commentText);
        book.setComments(comments);
        book = bookRep.save(book);

        return String.format(MSG_ADD_NEW_COMMENT, book.getBookName());
    }

    @Override
    public String getCommentsByBook(String bookName) {
        Book book = bookRep.findBookByBookName(bookName);
        if (book == null) {
            return MSG_DONT_FIND;
        }
        return showComments(book.getComments(), bookName);
    }

    @Override
    public String getBooksIsComment() {
        List<Book> listBooks = bookRep.findBooksBy();
        if (listBooks == null || listBooks.isEmpty()) {
            return MSG_DONT_FIND;
        }
        return MSG_SHOW_BOOKS_COMMENT + showBooks(listBooks);
    }

    private String showBooks(List<Book> listBooks) {
        if (listBooks == null || listBooks.isEmpty()) {
            return MSG_DONT_FIND;
        }
        StringBuffer stringBuffer = new StringBuffer();
        listBooks.forEach(stringBuffer::append);
        return stringBuffer.toString();
    }

    private String showComments(List<String> listComments, String bookName) {
        if (listComments == null || listComments.isEmpty()) {
            return MSG_DONT_FIND;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format(MSG_SHOW_COMMENT, bookName));
        listComments.forEach(v -> {
            stringBuffer.append(v).append("\n");
        });
        return stringBuffer.toString();
    }
}


