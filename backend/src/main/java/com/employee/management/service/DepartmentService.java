package com.employee.management.service;

import com.employee.management.dto.request.DepartmentRequestDTO;
import com.employee.management.dto.response.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO);

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO);

    void deleteDepartment(Long id);

    DepartmentResponseDTO getDepartmentById(Long id);

    List<DepartmentResponseDTO> getAllDepartments();

    DepartmentResponseDTO getDepartmentByName(String name);
}