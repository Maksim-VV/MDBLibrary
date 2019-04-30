package com.vasiliska.MDBLibrary.repository;


import com.vasiliska.MDBLibrary.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;


public interface BookRep extends MongoRepository<Book, String> {

    List<Book> findAll();

    Book findBookByBookName(String bookName);

    List<Book> findBooksByAuthor(String author);

    List<Book> findBookByGenre(String genre);

    int deleteBookByBookName(String bookName);

    @Query("{comments : {$exists:true}, $where:'this.comments.length > 0'}")
    List<Book> findBooksBy();
}
