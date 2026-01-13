package com.employee.management.service;

import com.employee.management.dto.request.EmployeeRequestDTO;
import com.employee.management.dto.response.EmployeeResponseDTO;
import com.employee.management.entity.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO);

    void deleteEmployee(Long id);

    EmployeeResponseDTO getEmployeeById(Long id);

    List<EmployeeResponseDTO> getAllEmployees();

    Page<EmployeeResponseDTO> getAllEmployeesPaginated(Pageable pageable);

    List<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId);

    List<EmployeeResponseDTO> getEmployeesByPosition(Long positionId);

    List<EmployeeResponseDTO> getEmployeesByStatus(EmployeeStatus status);

    List<EmployeeResponseDTO> searchEmployees(String keyword);
}