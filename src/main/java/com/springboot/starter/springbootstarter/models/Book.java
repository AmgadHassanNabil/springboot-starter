package com.springboot.starter.springbootstarter.models;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;

@Entity
public record Book(
                @Id Long id,
                String name,
                String isbn) {

}
