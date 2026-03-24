package com.library.service;

import com.library.dto.BookDTO;
import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBookById(Long id);
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO bookDTO);
    void deleteBook(Long id);
    List<BookDTO> searchBooks(String query);
    List<BookDTO> getBooksByGenre(String genre);
    List<BookDTO> getAvailableBooks();
}
