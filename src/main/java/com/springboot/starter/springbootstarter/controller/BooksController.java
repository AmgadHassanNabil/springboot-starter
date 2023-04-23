package com.springboot.starter.springbootstarter.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springboot.starter.springbootstarter.models.Book;
import com.springboot.starter.springbootstarter.services.BookService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BooksController {

    final BookService bookService;

    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> find(@PathVariable Long id) {
        Optional<Book> item = this.bookService.find(id);
        return ResponseEntity.of(item);
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book requestBook) {
        Book book = this.bookService.create(
                new Book(null, requestBook.getName(), requestBook.getIsbn(), requestBook.getInventoryCount(), null));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(book.getId())
                .toUri();
        return ResponseEntity.created(location).body(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book requestBook) {

        Optional<Book> updated = this.bookService.update(id, requestBook);

        return updated
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> {
                    Book created = this.bookService.create(requestBook);
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(created.getId())
                            .toUri();
                    return ResponseEntity.created(location).body(created);
                });
    }

    @GetMapping("/insufficientBooks")
    public ResponseEntity<List<Book>> insufficientBooks() {
        return ResponseEntity.ok(this.bookService.findInsufficientBooks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}