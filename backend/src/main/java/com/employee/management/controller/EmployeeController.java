package com.employee.management.controller;

import com.employee.management.dto.request.EmployeeRequestDTO;
import com.employee.management.dto.response.EmployeeResponseDTO;
import com.employee.management.entity.enums.EmployeeStatus;
import com.employee.management.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    private final EmployeeService employeeService;

    // CREATE
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO requestDTO) {

        log.info("REST request to create employee: {} {}",
                requestDTO.getFirstName(), requestDTO.getLastName());
        EmployeeResponseDTO response = employeeService.createEmployee(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO requestDTO) {

        log.info("REST request to update employee with ID: {}", id);
        EmployeeResponseDTO response = employeeService.updateEmployee(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    // DELETE (soft delete - changes status to TERMINATED)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("REST request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        log.info("REST request to get employee with ID: {}", id);
        EmployeeResponseDTO response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        log.info("REST request to get all employees");
        List<EmployeeResponseDTO> response = employeeService.getAllEmployees();
        return ResponseEntity.ok(response);
    }

    // GET ALL WITH PAGINATION
    @GetMapping("/paginated")
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        log.info("REST request to get employees page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeeResponseDTO> response = employeeService.getAllEmployeesPaginated(pageable);

        return ResponseEntity.ok(response);
    }

    // GET BY DEPARTMENT
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByDepartment(
            @PathVariable Long departmentId) {

        log.info("REST request to get employees for department ID: {}", departmentId);
        List<EmployeeResponseDTO> response = employeeService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(response);
    }

    // GET BY POSITION
    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByPosition(
            @PathVariable Long positionId) {

        log.info("REST request to get employees for position ID: {}", positionId);
        List<EmployeeResponseDTO> response = employeeService.getEmployeesByPosition(positionId);
        return ResponseEntity.ok(response);
    }

    // GET BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByStatus(
            @PathVariable EmployeeStatus status) {

        log.info("REST request to get employees with status: {}", status);
        List<EmployeeResponseDTO> response = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(response);
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDTO>> searchEmployees(
            @RequestParam String keyword) {

        log.info("REST request to search employees with keyword: {}", keyword);
        List<EmployeeResponseDTO> response = employeeService.searchEmployees(keyword);
        return ResponseEntity.ok(response);
    }
}