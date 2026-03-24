package com.library.service.impl;

import com.library.dto.BookDTO;
import com.library.entity.Book;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return new BookDTO(book);
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        if (bookDTO.getIsbn() != null && !bookDTO.getIsbn().isEmpty()
                && bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new BadRequestException("A book with ISBN '" + bookDTO.getIsbn() + "' already exists.");
        }
        Book book = bookDTO.toEntity();
        Book saved = bookRepository.save(book);
        return new BookDTO(saved);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        // Check ISBN uniqueness (excluding current book)
        if (bookDTO.getIsbn() != null && !bookDTO.getIsbn().isEmpty()) {
            bookRepository.findByIsbn(bookDTO.getIsbn()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new BadRequestException("A book with ISBN '" + bookDTO.getIsbn() + "' already exists.");
                }
            });
        }

        // Update fields
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setGenre(bookDTO.getGenre());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedYear(bookDTO.getPublishedYear());
        book.setDescription(bookDTO.getDescription());
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());

        // Update copies carefully
        int diff = bookDTO.getTotalCopies() - book.getTotalCopies();
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(Math.max(0, book.getAvailableCopies() + diff));

        // Update status based on availability
        if (book.getAvailableCopies() > 0) {
            book.setStatus(Book.BookStatus.AVAILABLE);
        } else {
            book.setStatus(Book.BookStatus.UNAVAILABLE);
        }

        Book updated = bookRepository.save(book);
        return new BookDTO(updated);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> searchBooks(String query) {
        return bookRepository.searchBooks(query)
                .stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getBooksByGenre(String genre) {
        return bookRepository.findByGenreIgnoreCase(genre)
                .stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findByStatus(Book.BookStatus.AVAILABLE)
                .stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }
}
