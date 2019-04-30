package com.vasiliska.MDBLibrary.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "books")
public class Book {

    @Id
    private String bookId;
    private String bookName;
    private String author;
    private String genre;
    private List<String> comments;

    public Book(String bookName, String author, String genre) {
        this.bookName = bookName;
        this.author = author;
        this.genre = genre;
    }

    public Book() {
    }

    @Override
    public String toString() {
        return bookName + " " + author + " " + genre +"\n";
    }

}
