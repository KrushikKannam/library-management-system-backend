package com.library.dto;

public class DashboardStatsDTO {

    private long totalBooks;
    private long totalMembers;
    private long totalBorrows;
    private long activeBorrows;
    private long overdueBooks;
    private long availableBooks;
    private long activeMembers;
    private double totalFinesCollected;

    // ---- Constructors ----
    public DashboardStatsDTO() {}

    // ---- Getters & Setters ----
    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }

    public long getTotalMembers() { return totalMembers; }
    public void setTotalMembers(long totalMembers) { this.totalMembers = totalMembers; }

    public long getTotalBorrows() { return totalBorrows; }
    public void setTotalBorrows(long totalBorrows) { this.totalBorrows = totalBorrows; }

    public long getActiveBorrows() { return activeBorrows; }
    public void setActiveBorrows(long activeBorrows) { this.activeBorrows = activeBorrows; }

    public long getOverdueBooks() { return overdueBooks; }
    public void setOverdueBooks(long overdueBooks) { this.overdueBooks = overdueBooks; }

    public long getAvailableBooks() { return availableBooks; }
    public void setAvailableBooks(long availableBooks) { this.availableBooks = availableBooks; }

    public long getActiveMembers() { return activeMembers; }
    public void setActiveMembers(long activeMembers) { this.activeMembers = activeMembers; }

    public double getTotalFinesCollected() { return totalFinesCollected; }
    public void setTotalFinesCollected(double totalFinesCollected) { this.totalFinesCollected = totalFinesCollected; }
}
