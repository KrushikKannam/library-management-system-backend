package com.library.service.impl;

import com.library.dto.BorrowRecordDTO;
import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Member;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private static final double FINE_PER_DAY = 5.0; // ₹5 per day
    private static final int DEFAULT_DUE_DAYS = 14;

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BorrowServiceImpl(BorrowRecordRepository borrowRecordRepository,
                              BookRepository bookRepository,
                              MemberRepository memberRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDTO> getAllBorrowRecords() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(BorrowRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BorrowRecordDTO getBorrowRecordById(Long id) {
        BorrowRecord record = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record", "id", id));
        return new BorrowRecordDTO(record);
    }

    @Override
    public BorrowRecordDTO borrowBook(Long bookId, Long memberId, Integer dueDays) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));

        // Validate member is active
        if (member.getStatus() != Member.MemberStatus.ACTIVE) {
            throw new BadRequestException("Member account is " + member.getStatus().name().toLowerCase() +
                    ". Only ACTIVE members can borrow books.");
        }

        // Validate book is available
        if (book.getAvailableCopies() <= 0) {
            throw new BadRequestException("No available copies of '" + book.getTitle() + "'.");
        }

        // Check if member already has this book borrowed
        if (borrowRecordRepository.existsByBookIdAndMemberIdAndStatus(
                bookId, memberId, BorrowRecord.BorrowStatus.BORROWED)) {
            throw new BadRequestException("Member already has a copy of '" + book.getTitle() + "' borrowed.");
        }

        int loanDays = (dueDays != null && dueDays > 0) ? dueDays : DEFAULT_DUE_DAYS;
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(loanDays);

        BorrowRecord record = new BorrowRecord(book, member, today, dueDate);
        BorrowRecord saved = borrowRecordRepository.save(record);

        // Decrease available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        if (book.getAvailableCopies() == 0) {
            book.setStatus(Book.BookStatus.UNAVAILABLE);
        }
        bookRepository.save(book);

        return new BorrowRecordDTO(saved);
    }

    @Override
    public BorrowRecordDTO returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record", "id", borrowRecordId));

        if (record.getStatus() == BorrowRecord.BorrowStatus.RETURNED) {
            throw new BadRequestException("This book has already been returned.");
        }

        LocalDate today = LocalDate.now();
        record.setReturnDate(today);
        record.setStatus(BorrowRecord.BorrowStatus.RETURNED);

        // Calculate fine if overdue
        if (today.isAfter(record.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), today);
            record.setFineAmount(overdueDays * FINE_PER_DAY);
        }

        BorrowRecord updated = borrowRecordRepository.save(record);

        // Increase available copies
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setStatus(Book.BookStatus.AVAILABLE);
        bookRepository.save(book);

        return new BorrowRecordDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDTO> getBorrowsByMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member", "id", memberId);
        }
        return borrowRecordRepository.findByMemberId(memberId)
                .stream()
                .map(BorrowRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDTO> getBorrowsByBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book", "id", bookId);
        }
        return borrowRecordRepository.findByBookId(bookId)
                .stream()
                .map(BorrowRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDTO> getOverdueRecords() {
        return borrowRecordRepository.findOverdueRecords(LocalDate.now())
                .stream()
                .map(BorrowRecordDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDTO> getActiveBorrows() {
        return borrowRecordRepository.findByStatus(BorrowRecord.BorrowStatus.BORROWED)
                .stream()
                .map(BorrowRecordDTO::new)
                .collect(Collectors.toList());
    }
}
