package com.library.service;

import com.library.dto.BorrowRecordDTO;
import java.util.List;

public interface BorrowService {
    List<BorrowRecordDTO> getAllBorrowRecords();
    BorrowRecordDTO getBorrowRecordById(Long id);
    BorrowRecordDTO borrowBook(Long bookId, Long memberId, Integer dueDays);
    BorrowRecordDTO returnBook(Long borrowRecordId);
    List<BorrowRecordDTO> getBorrowsByMember(Long memberId);
    List<BorrowRecordDTO> getBorrowsByBook(Long bookId);
    List<BorrowRecordDTO> getOverdueRecords();
    List<BorrowRecordDTO> getActiveBorrows();
}
