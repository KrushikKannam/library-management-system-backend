package com.library.service.impl;

import com.library.dto.DashboardStatsDTO;
import com.library.entity.Member;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Autowired
    public DashboardServiceImpl(BookRepository bookRepository,
                                 MemberRepository memberRepository,
                                 BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalBooks(bookRepository.count());
        stats.setTotalMembers(memberRepository.count());
        stats.setTotalBorrows(borrowRecordRepository.count());
        stats.setActiveBorrows(borrowRecordRepository.countActiveBorrows());
        stats.setOverdueBooks(borrowRecordRepository.countOverdueRecords(LocalDate.now()));
        stats.setAvailableBooks(bookRepository.countAvailableBooks());
        stats.setActiveMembers(memberRepository.countByStatus(Member.MemberStatus.ACTIVE));
        stats.setTotalFinesCollected(borrowRecordRepository.sumFinesCollected());

        return stats;
    }
}
