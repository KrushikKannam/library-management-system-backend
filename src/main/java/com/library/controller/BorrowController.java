package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.BorrowRecordDTO;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = "http://localhost:5173")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowRecordDTO>>> getAllBorrowRecords() {
        List<BorrowRecordDTO> records = borrowService.getAllBorrowRecords();
        return ResponseEntity.ok(ApiResponse.success("Borrow records fetched successfully", records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowRecordDTO>> getBorrowRecordById(@PathVariable Long id) {
        BorrowRecordDTO record = borrowService.getBorrowRecordById(id);
        return ResponseEntity.ok(ApiResponse.success("Borrow record fetched successfully", record));
    }

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse<BorrowRecordDTO>> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long memberId,
            @RequestParam(required = false, defaultValue = "14") Integer dueDays) {
        BorrowRecordDTO record = borrowService.borrowBook(bookId, memberId, dueDays);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book borrowed successfully", record));
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<ApiResponse<BorrowRecordDTO>> returnBook(@PathVariable Long id) {
        BorrowRecordDTO record = borrowService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully", record));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<BorrowRecordDTO>>> getBorrowsByMember(
            @PathVariable Long memberId) {
        List<BorrowRecordDTO> records = borrowService.getBorrowsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("Member borrows fetched successfully", records));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<BorrowRecordDTO>>> getBorrowsByBook(
            @PathVariable Long bookId) {
        List<BorrowRecordDTO> records = borrowService.getBorrowsByBook(bookId);
        return ResponseEntity.ok(ApiResponse.success("Book borrows fetched successfully", records));
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<BorrowRecordDTO>>> getOverdueRecords() {
        List<BorrowRecordDTO> records = borrowService.getOverdueRecords();
        return ResponseEntity.ok(ApiResponse.success("Overdue records fetched successfully", records));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<BorrowRecordDTO>>> getActiveBorrows() {
        List<BorrowRecordDTO> records = borrowService.getActiveBorrows();
        return ResponseEntity.ok(ApiResponse.success("Active borrows fetched successfully", records));
    }
}
