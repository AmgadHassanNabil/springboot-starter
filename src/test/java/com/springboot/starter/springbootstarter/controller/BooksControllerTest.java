package com.springboot.starter.springbootstarter.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.starter.springbootstarter.models.Book;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BooksControllerTest {

    @InjectMocks
    BooksController subject;

    MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
        BooksController.allBooks = new ArrayList<>(
                List.of(
                        new Book(0L, "Harry Potter", "123-54231"),
                        new Book(1L, "Lord Of the Rings", "100-54321")));
    }

    @Test
    void testCreate() throws JsonProcessingException, Exception {

        Book request = new Book(null, "Tale Of Two Cities", "100-0009990");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(post("/api/v1/books").headers(headers)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Tale Of Two Cities"))
                .andExpect(jsonPath("$.isbn").value("100-0009990"))
                .andExpect(header().exists("Location"));

    }

    @Test
    void testDelete() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(delete("/api/v1/books/1").headers(headers))
                .andExpect(status().isNoContent());

    }

    @Test
    void testFind() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(get("/api/v1/books/1").headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Lord Of the Rings"))
                .andExpect(jsonPath("$.isbn").value("100-54321"));
    }

    @Test
    void testFindAll() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(get("/api/v1/books").headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[0].isbn").value("123-54231"));

    }

    @Test
    void testUpdate() throws Exception {

        Book request = new Book(null, "Tale Of Two Cities", null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(put("/api/v1/books/1").headers(headers)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Tale Of Two Cities"))
                .andExpect(jsonPath("$.isbn").value("100-54321"));

    }
}
