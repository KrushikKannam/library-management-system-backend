package com.library.repository;

import com.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByMemberId(Long memberId);

    List<BorrowRecord> findByBookId(Long bookId);

    List<BorrowRecord> findByStatus(BorrowRecord.BorrowStatus status);

    @Query("SELECT br FROM BorrowRecord br WHERE br.member.id = :memberId AND br.status = 'BORROWED'")
    List<BorrowRecord> findActiveBorrowsByMember(@Param("memberId") Long memberId);

    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate < :today AND br.status = 'BORROWED'")
    List<BorrowRecord> findOverdueRecords(@Param("today") LocalDate today);

    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.status = 'BORROWED'")
    long countActiveBorrows();

    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.dueDate < :today AND br.status = 'BORROWED'")
    long countOverdueRecords(@Param("today") LocalDate today);

    @Query("SELECT COALESCE(SUM(br.fineAmount), 0) FROM BorrowRecord br WHERE br.status = 'RETURNED'")
    double sumFinesCollected();

    boolean existsByBookIdAndMemberIdAndStatus(Long bookId, Long memberId, BorrowRecord.BorrowStatus status);
}
