package com.employee.management.dto.response;

import com.employee.management.entity.enums.EmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String address;
    private EmployeeStatus status;


    private Long departmentId;
    private String departmentName;


    private Long positionId;
    private String positionTitle;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}