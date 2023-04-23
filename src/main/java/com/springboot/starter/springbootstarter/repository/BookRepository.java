package com.springboot.starter.springbootstarter.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.starter.springbootstarter.models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query(value = "SELECT b FROM Book b WHERE b.inventoryCount > ?1")
    List<Book> findAllBooksWithInventoryCount(Integer inventoryCount, Sort sort);
}
