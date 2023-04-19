package com.springboot.starter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springboot.starter.springbootstarter.models.Book;
import com.springboot.starter.springbootstarter.repository.BookRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository repository;

    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        Iterable<Book> items = repository.findAll();
        items.forEach(list::add);
        return list;
    }

    public Optional<Book> find(Long id) {
        return repository.findById(id);
    }

    public Book create(Book book) {
        Book newBook = new Book(null, book.name(), book.isbn());
        return this.repository.save(newBook);
    }

    public Optional<Book> update(Long id, Book newBook) {
        return repository.findById(id)
                .map(oldBook -> {
                    Book updated = new Book(oldBook.id(), newBook.name(), newBook.isbn());
                    return repository.save(updated);
                });
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}
