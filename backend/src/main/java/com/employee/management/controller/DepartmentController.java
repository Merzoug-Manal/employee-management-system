package com.employee.management.controller;

import com.employee.management.dto.request.DepartmentRequestDTO;
import com.employee.management.dto.response.DepartmentResponseDTO;
import com.employee.management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentController {

    private final DepartmentService departmentService;

    // CREATE - POST /api/departments
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {

        log.info("REST request to create department: {}", requestDTO.getName());
        DepartmentResponseDTO response = departmentService.createDepartment(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE - PUT /api/departments/{id}
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDTO requestDTO) {

        log.info("REST request to update department with ID: {}", id);
        DepartmentResponseDTO response = departmentService.updateDepartment(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    // DELETE - DELETE /api/departments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.info("REST request to delete department with ID: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // GET ONE - GET /api/departments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) {
        log.info("REST request to get department with ID: {}", id);
        DepartmentResponseDTO response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }

    // GET ALL - GET /api/departments
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        log.info("REST request to get all departments");
        List<DepartmentResponseDTO> response = departmentService.getAllDepartments();
        return ResponseEntity.ok(response);
    }

    // GET BY NAME - GET /api/departments/name/{name}
    @GetMapping("/name/{name}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentByName(@PathVariable String name) {
        log.info("REST request to get department by name: {}", name);
        DepartmentResponseDTO response = departmentService.getDepartmentByName(name);
        return ResponseEntity.ok(response);
    }
}