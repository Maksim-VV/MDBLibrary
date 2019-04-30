package com.vasiliska.MDBLibrary.service;


public interface ShellService {

    String addNewBook(String bookName, String authorName, String genreName);
    String bookByName(String bookName);
    String delBook(String bookName);

    String bookByGenre(String genre);
    String bookByAuthor(String author);
    String showAllBooks();

    String addComment(String commentText, String bookName);
    String getCommentsByBook(String bookName);

    String getBooksIsComment();
}
