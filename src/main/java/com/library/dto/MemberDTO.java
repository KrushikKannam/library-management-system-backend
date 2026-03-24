package com.library.dto;

import com.library.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class MemberDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;
    private String address;
    private Member.MemberStatus status;
    private LocalDateTime membershipDate;
    private LocalDateTime updatedAt;
    private int activeBorrows;

    // ---- Constructors ----
    public MemberDTO() {}

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.firstName = member.getFirstName();
        this.lastName = member.getLastName();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.address = member.getAddress();
        this.status = member.getStatus();
        this.membershipDate = member.getMembershipDate();
        this.updatedAt = member.getUpdatedAt();
    }

    // ---- Convert DTO to Entity ----
    public Member toEntity() {
        Member member = new Member();
        member.setFirstName(this.firstName);
        member.setLastName(this.lastName);
        member.setEmail(this.email);
        member.setPhone(this.phone);
        member.setAddress(this.address);
        return member;
    }

    // ---- Getters & Setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Member.MemberStatus getStatus() { return status; }
    public void setStatus(Member.MemberStatus status) { this.status = status; }

    public LocalDateTime getMembershipDate() { return membershipDate; }
    public void setMembershipDate(LocalDateTime membershipDate) { this.membershipDate = membershipDate; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getActiveBorrows() { return activeBorrows; }
    public void setActiveBorrows(int activeBorrows) { this.activeBorrows = activeBorrows; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
