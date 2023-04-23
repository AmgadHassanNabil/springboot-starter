package com.springboot.starter.springbootstarter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
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

    public Optional<Book> find(long id) {
        return repository.findById(id);
    }

    public Book create(Book book) {
        Book newBook = new Book(null, book.getName(), book.getIsbn(), book.getInventoryCount(), false);
        return this.repository.save(newBook);
    }

    public Optional<Book> update(Long id, Book newBook) {
        return repository.findById(id)
                .map(oldBook -> {
                    Book updated = new Book(oldBook.getId(),
                            newBook.getName() == null ? oldBook.getName() : newBook.getName(),
                            newBook.getIsbn() == null ? oldBook.getIsbn() : newBook.getIsbn(),
                            newBook.getInventoryCount() == null ? oldBook.getInventoryCount()
                                    : newBook.getInventoryCount(),
                            newBook.getDeleted() == null ? oldBook.getDeleted() : newBook.getDeleted());
                    return repository.save(updated);
                });
    }

    public List<Book> findInsufficientBooks() {
        return this.repository.findAllBooksWithInventoryCount(3, Sort.by("isbn"));
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}
