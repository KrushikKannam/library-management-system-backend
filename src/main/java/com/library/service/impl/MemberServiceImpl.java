package com.library.service.impl;

import com.library.dto.MemberDTO;
import com.library.entity.BorrowRecord;
import com.library.entity.Member;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                              BorrowRecordRepository borrowRecordRepository) {
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(member -> {
                    MemberDTO dto = new MemberDTO(member);
                    int active = borrowRecordRepository.findActiveBorrowsByMember(member.getId()).size();
                    dto.setActiveBorrows(active);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
        MemberDTO dto = new MemberDTO(member);
        int active = borrowRecordRepository.findActiveBorrowsByMember(id).size();
        dto.setActiveBorrows(active);
        return dto;
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new BadRequestException("A member with email '" + memberDTO.getEmail() + "' already exists.");
        }
        Member member = memberDTO.toEntity();
        Member saved = memberRepository.save(member);
        return new MemberDTO(saved);
    }

    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        // Check email uniqueness (excluding current member)
        memberRepository.findByEmail(memberDTO.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BadRequestException("A member with email '" + memberDTO.getEmail() + "' already exists.");
            }
        });

        member.setFirstName(memberDTO.getFirstName());
        member.setLastName(memberDTO.getLastName());
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setAddress(memberDTO.getAddress());

        Member updated = memberRepository.save(member);
        return new MemberDTO(updated);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));

        // Check if member has active borrows
        List<BorrowRecord> activeBorrows = borrowRecordRepository.findActiveBorrowsByMember(id);
        if (!activeBorrows.isEmpty()) {
            throw new BadRequestException("Cannot delete member with " + activeBorrows.size() + " active borrow(s). Please return all books first.");
        }

        memberRepository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> searchMembers(String query) {
        return memberRepository.searchMembers(query)
                .stream()
                .map(MemberDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> getMembersByStatus(String status) {
        try {
            Member.MemberStatus memberStatus = Member.MemberStatus.valueOf(status.toUpperCase());
            return memberRepository.findByStatus(memberStatus)
                    .stream()
                    .map(MemberDTO::new)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". Valid values: ACTIVE, SUSPENDED, EXPIRED");
        }
    }

    @Override
    public MemberDTO updateMemberStatus(Long id, String status) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
        try {
            Member.MemberStatus memberStatus = Member.MemberStatus.valueOf(status.toUpperCase());
            member.setStatus(memberStatus);
            Member updated = memberRepository.save(member);
            return new MemberDTO(updated);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". Valid values: ACTIVE, SUSPENDED, EXPIRED");
        }
    }
}
