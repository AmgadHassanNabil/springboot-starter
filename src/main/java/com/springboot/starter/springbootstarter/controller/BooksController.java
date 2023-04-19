package com.springboot.starter.springbootstarter.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

@RestController
@RequestMapping("/api/v1/books")
public class BooksController {

    static List<Book> allBooks = new ArrayList<>(
            List.of(
                    new Book(0L, "Harry Potter", "123-54231"),
                    new Book(1L, "Lord Of the Rings", "100-54321")));

    @GetMapping
    public List<Book> findAll() {
        return allBooks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> find(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(allBooks.stream().filter(book -> book.id() == id).findFirst().orElseThrow());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book requestBook) {
        Integer id = allBooks.size();
        Book book = new Book(id.longValue(), requestBook.name(), requestBook.isbn());
        allBooks.add(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book requestBook) {
        int i = 0;
        for (; i < allBooks.size(); i++)
            if (allBooks.get(i).id() == id) {
                Book book = new Book(id, requestBook.name() == null ? allBooks.get(i).name() : requestBook.name(),
                        requestBook.isbn() == null ? allBooks.get(i).isbn() : requestBook.isbn());

                allBooks.set(i, book);
                break;
            }

        return ResponseEntity.ok().body(allBooks.get(i));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        allBooks.removeIf(book -> book.id() == id);
        return ResponseEntity.noContent().build();
    }
}