package com.vasiliska.MDBLibrary.service;


import com.vasiliska.MDBLibrary.domain.Book;
import com.vasiliska.MDBLibrary.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShellServiceImpl implements ShellService {

    private BookRepository bookRepository;

    @Autowired
    public ShellServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
        Book book = bookRepository.findBookByBookName(bookName);
        if (book != null) {
            if (book.getAuthor().equals(authorName) && book.getGenre().equals(genreName)) {
                return String.format(MSG_BOOK_IS_EXIST, bookName);
            }
        }

        bookRepository.save(new Book(bookName, authorName, genreName));
        return String.format(MSG_ADD_NEW_BOOK, bookName);
    }

    @Override
    public String bookByName(String bookName) {
        val book = bookRepository.findBookByBookName(bookName);
        if (book == null) {
            return MSG_DONT_FIND;
        }

        return book.toString();
    }

    @Override
    public String delBook(String bookName) {
        if (bookRepository.deleteBookByBookName(bookName) > 0) {
            return String.format(MSG_DELETE_BOOK, bookName);
        }
        return String.format(MSG_DONT_FIND, bookName);
    }

    @Override
    public String bookByGenre(String genre) {
        return showBooks(bookRepository.findBookByGenre(genre));
    }

    @Override
    public String bookByAuthor(String author) {
        return showBooks(bookRepository.findBooksByAuthor(author));
    }

    @Override
    public String showAllBooks() {
        return showBooks(bookRepository.findAll());
    }

    @Override
    public String addComment(String commentText, String bookName) {
        Book book = bookRepository.findBookByBookName(bookName);
        if (book == null) {
            return MSG_DONT_FIND;
        }

        List<String> comments = book.getComments();
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(commentText);
        book.setComments(comments);
        book = bookRepository.save(book);

        return String.format(MSG_ADD_NEW_COMMENT, book.getBookName());
    }

    @Override
    public String getCommentsByBook(String bookName) {
        Book book = bookRepository.findBookByBookName(bookName);
        if (book == null) {
            return MSG_DONT_FIND;
        }
        return showComments(book.getComments(), bookName);
    }

    @Override
    public String getBooksIsComment() {
        List<Book> listBooks = bookRepository.findBooksWthisComment();
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


