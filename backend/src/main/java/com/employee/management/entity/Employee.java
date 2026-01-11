package com.employee.management.entity;

import com.employee.management.entity.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employee extends BaseEntity {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<LeaveBalance> leaveBalances = new ArrayList<>();
}