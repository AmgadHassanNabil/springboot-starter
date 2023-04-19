package com.springboot.starter.springbootstarter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.starter.springbootstarter.models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

}
