package com.library.service;

import com.library.dto.MemberDTO;
import java.util.List;

public interface MemberService {
    List<MemberDTO> getAllMembers();
    MemberDTO getMemberById(Long id);
    MemberDTO createMember(MemberDTO memberDTO);
    MemberDTO updateMember(Long id, MemberDTO memberDTO);
    void deleteMember(Long id);
    List<MemberDTO> searchMembers(String query);
    List<MemberDTO> getMembersByStatus(String status);
    MemberDTO updateMemberStatus(Long id, String status);
}
