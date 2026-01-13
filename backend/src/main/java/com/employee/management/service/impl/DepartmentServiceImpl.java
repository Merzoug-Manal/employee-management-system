package com.employee.management.service.impl;

import com.employee.management.dto.request.DepartmentRequestDTO;
import com.employee.management.dto.response.DepartmentResponseDTO;
import com.employee.management.entity.Department;
import com.employee.management.repository.DepartmentRepository;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO) {
        log.info("Creating new department: {}", requestDTO.getName());

        // Check if department name already exists
        if (departmentRepository.existsByName(requestDTO.getName())) {
            throw new RuntimeException("Department with name '" + requestDTO.getName() + "' already exists");
        }

        // Create entity from DTO
        Department department = new Department();
        department.setName(requestDTO.getName());
        department.setDescription(requestDTO.getDescription());

        // Save to database
        Department savedDepartment = departmentRepository.save(department);

        log.info("Department created successfully with ID: {}", savedDepartment.getId());
        return convertToResponseDTO(savedDepartment);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO) {
        log.info("Updating department with ID: {}", id);

        // Find existing department
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

        // Check if new name conflicts with another department
        if (!department.getName().equals(requestDTO.getName()) &&
                departmentRepository.existsByName(requestDTO.getName())) {
            throw new RuntimeException("Department with name '" + requestDTO.getName() + "' already exists");
        }

        // Update fields
        department.setName(requestDTO.getName());
        department.setDescription(requestDTO.getDescription());

        // Save changes
        Department updatedDepartment = departmentRepository.save(department);

        log.info("Department updated successfully");
        return convertToResponseDTO(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        log.info("Deleting department with ID: {}", id);

        // Check if department exists
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

        // Check if department has employees
        long employeeCount = employeeRepository.countByDepartmentId(id);
        if (employeeCount > 0) {
            throw new RuntimeException("Cannot delete department with " + employeeCount + " employees. " +
                    "Please reassign employees first.");
        }

        // Delete department
        departmentRepository.delete(department);

        log.info("Department deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentById(Long id) {
        log.info("Fetching department with ID: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));

        return convertToResponseDTO(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAllDepartments() {
        log.info("Fetching all departments");

        return departmentRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getDepartmentByName(String name) {
        log.info("Fetching department by name: {}", name);

        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Department not found with name: " + name));

        return convertToResponseDTO(department);
    }

    // Helper method to convert Entity to DTO
    private DepartmentResponseDTO convertToResponseDTO(Department department) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        dto.setEmployeeCount(employeeRepository.countByDepartmentId(department.getId()).intValue());
        dto.setCreatedAt(department.getCreatedAt());
        dto.setUpdatedAt(department.getUpdatedAt());
        return dto;
    }
}