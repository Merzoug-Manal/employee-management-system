package com.employee.management.service.impl;

import com.employee.management.dto.request.EmployeeRequestDTO;
import com.employee.management.dto.response.EmployeeResponseDTO;
import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.entity.Position;
import com.employee.management.entity.enums.EmployeeStatus;
import com.employee.management.repository.DepartmentRepository;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.PositionRepository;
import com.employee.management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        log.info("Creating new employee: {} {}", requestDTO.getFirstName(), requestDTO.getLastName());

        // Check if email already exists
        if (employeeRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Employee with email '" + requestDTO.getEmail() + "' already exists");
        }

        // Create entity
        Employee employee = new Employee();
        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setEmail(requestDTO.getEmail());
        employee.setPhone(requestDTO.getPhone());
        employee.setDateOfBirth(requestDTO.getDateOfBirth());
        employee.setHireDate(requestDTO.getHireDate());
        employee.setSalary(requestDTO.getSalary());
        employee.setAddress(requestDTO.getAddress());
        employee.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : EmployeeStatus.ACTIVE);

        // Set department if provided
        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with ID: " + requestDTO.getDepartmentId()));
            employee.setDepartment(department);
        }

        // Set position if provided
        if (requestDTO.getPositionId() != null) {
            Position position = positionRepository.findById(requestDTO.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found with ID: " + requestDTO.getPositionId()));
            employee.setPosition(position);
        }

        // Save
        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        return convertToResponseDTO(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        log.info("Updating employee with ID: {}", id);

        // Find existing employee
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        // Check email conflicts
        if (!employee.getEmail().equals(requestDTO.getEmail()) &&
                employeeRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Employee with email '" + requestDTO.getEmail() + "' already exists");
        }

        // Update fields
        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setEmail(requestDTO.getEmail());
        employee.setPhone(requestDTO.getPhone());
        employee.setDateOfBirth(requestDTO.getDateOfBirth());
        employee.setHireDate(requestDTO.getHireDate());
        employee.setSalary(requestDTO.getSalary());
        employee.setAddress(requestDTO.getAddress());

        if (requestDTO.getStatus() != null) {
            employee.setStatus(requestDTO.getStatus());
        }

        // Update department
        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with ID: " + requestDTO.getDepartmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        // Update position
        if (requestDTO.getPositionId() != null) {
            Position position = positionRepository.findById(requestDTO.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found with ID: " + requestDTO.getPositionId()));
            employee.setPosition(position);
        } else {
            employee.setPosition(null);
        }

        // Save
        Employee updatedEmployee = employeeRepository.save(employee);

        log.info("Employee updated successfully");
        return convertToResponseDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        // Soft delete - just change status instead of actually deleting
        employee.setStatus(EmployeeStatus.TERMINATED);
        employeeRepository.save(employee);

        log.info("Employee status changed to TERMINATED");
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        return convertToResponseDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        log.info("Fetching all employees");

        return employeeRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmployeesPaginated(Pageable pageable) {
        log.info("Fetching employees page: {}", pageable.getPageNumber());

        return employeeRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId) {
        log.info("Fetching employees for department ID: {}", departmentId);

        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByPosition(Long positionId) {
        log.info("Fetching employees for position ID: {}", positionId);

        return employeeRepository.findByPositionId(positionId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByStatus(EmployeeStatus status) {
        log.info("Fetching employees with status: {}", status);

        return employeeRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> searchEmployees(String keyword) {
        log.info("Searching employees with keyword: {}", keyword);

        return employeeRepository.searchEmployees(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method
    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setHireDate(employee.getHireDate());
        dto.setSalary(employee.getSalary());
        dto.setAddress(employee.getAddress());
        dto.setStatus(employee.getStatus());

        // Department info
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }

        // Position info
        if (employee.getPosition() != null) {
            dto.setPositionId(employee.getPosition().getId());
            dto.setPositionTitle(employee.getPosition().getTitle());
        }

        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        return dto;
    }}