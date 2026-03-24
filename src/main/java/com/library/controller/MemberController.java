package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.MemberDTO;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:5173")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberDTO>>> getAllMembers() {
        List<MemberDTO> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success("Members fetched successfully", members));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberDTO>> getMemberById(@PathVariable Long id) {
        MemberDTO member = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success("Member fetched successfully", member));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MemberDTO>> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        MemberDTO created = memberService.createMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberDTO>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberDTO memberDTO) {
        MemberDTO updated = memberService.updateMember(id, memberDTO);
        return ResponseEntity.ok(ApiResponse.success("Member updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.success("Member deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MemberDTO>>> searchMembers(@RequestParam String query) {
        List<MemberDTO> members = memberService.searchMembers(query);
        return ResponseEntity.ok(ApiResponse.success("Search results", members));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<MemberDTO>>> getMembersByStatus(@PathVariable String status) {
        List<MemberDTO> members = memberService.getMembersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Members by status fetched successfully", members));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MemberDTO>> updateMemberStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        MemberDTO updated = memberService.updateMemberStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Member status updated successfully", updated));
    }
}
