package com.library.api.controller;

import com.library.api.model.Book;
import com.library.api.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("1234567890");
        testBook.setYear(2024);
        testBook.setCopies(5);
    }

    @Test
    void whenGetAllBooks_thenReturnBooksList() throws Exception {
        List<Book> books = Arrays.asList(testBook);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(testBook.getTitle())));
    }

    @Test
    void whenGetBookById_thenReturnBook() throws Exception {
        when(bookService.getBookById(anyLong())).thenReturn(Optional.of(testBook));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testBook.getTitle())))
                .andExpect(jsonPath("$.author", is(testBook.getAuthor())));
    }

    @Test
    void whenGetBookById_thenReturnNotFound() throws Exception {
        when(bookService.getBookById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateBook_thenReturnCreatedBook() throws Exception {
        when(bookService.saveBook(any(Book.class))).thenReturn(testBook);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"title\":\"Test Book\",\"author\":\"Test Author\",\"isbn\":\"1234567890\",\"year\":2024,\"copies\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(testBook.getTitle())));
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBook() throws Exception {
        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(testBook);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"title\":\"Updated Book\",\"author\":\"Test Author\",\"isbn\":\"1234567890\",\"year\":2024,\"copies\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testBook.getTitle())));
    }

    @Test
    void whenDeleteBook_thenReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBook(anyLong());

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenSearchBooksByTitle_thenReturnMatchingBooks() throws Exception {
        List<Book> books = Arrays.asList(testBook);
        when(bookService.findBooksByTitle(anyString())).thenReturn(books);

        mockMvc.perform(get("/api/books/search/title")
                .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(testBook.getTitle())));
    }

    @Test
    void whenSearchBooksByAuthor_thenReturnMatchingBooks() throws Exception {
        List<Book> books = Arrays.asList(testBook);
        when(bookService.findBooksByAuthor(anyString())).thenReturn(books);

        mockMvc.perform(get("/api/books/search/author")
                .param("author", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is(testBook.getAuthor())));
    }

    @Test
    void whenGetAvailableBooks_thenReturnAvailableBooks() throws Exception {
        List<Book> books = Arrays.asList(testBook);
        when(bookService.findAvailableBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].copies", greaterThan(0)));
    }

    @Test
    void whenCheckBookAvailability_thenReturnAvailability() throws Exception {
        when(bookService.isBookAvailable(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/books/check-availability")
                .param("isbn", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void whenUpdateBookCopies_thenReturnOk() throws Exception {
        doNothing().when(bookService).updateBookCopies(anyString(), anyInt());

        mockMvc.perform(put("/api/books/1234567890/copies")
                .param("copies", "10"))
                .andExpect(status().isOk());
    }
}