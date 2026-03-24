package com.library.dto;

import com.library.entity.BorrowRecord;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BorrowRecordDTO {

    private Long id;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    private String bookTitle;
    private String bookAuthor;
    private String bookIsbn;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    private String memberName;
    private String memberEmail;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private BorrowRecord.BorrowStatus status;
    private Double fineAmount;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ---- Constructors ----
    public BorrowRecordDTO() {}

    public BorrowRecordDTO(BorrowRecord record) {
        this.id = record.getId();
        this.bookId = record.getBook().getId();
        this.bookTitle = record.getBook().getTitle();
        this.bookAuthor = record.getBook().getAuthor();
        this.bookIsbn = record.getBook().getIsbn();
        this.memberId = record.getMember().getId();
        this.memberName = record.getMember().getFullName();
        this.memberEmail = record.getMember().getEmail();
        this.borrowDate = record.getBorrowDate();
        this.dueDate = record.getDueDate();
        this.returnDate = record.getReturnDate();
        this.status = record.getStatus();
        this.fineAmount = record.getFineAmount();
        this.notes = record.getNotes();
        this.createdAt = record.getCreatedAt();
        this.updatedAt = record.getUpdatedAt();
    }

    // ---- Getters & Setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public BorrowRecord.BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowRecord.BorrowStatus status) { this.status = status; }

    public Double getFineAmount() { return fineAmount; }
    public void setFineAmount(Double fineAmount) { this.fineAmount = fineAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
