package com.employee.management.entity;

import com.employee.management.entity.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "leave_balances",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "year", "leave_type"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeaveBalance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false, length = 20)
    private LeaveType leaveType;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @Column(name = "used_days", nullable = false)
    private Integer usedDays = 0;

    @Column(name = "remaining_days", nullable = false)
    private Integer remainingDays;

    @PrePersist
    @PreUpdate
    public void calculateRemainingDays() {
        this.remainingDays = this.totalDays - this.usedDays;
    }
}