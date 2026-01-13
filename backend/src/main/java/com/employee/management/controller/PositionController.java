package com.employee.management.controller;

import com.employee.management.dto.request.PositionRequestDTO;
import com.employee.management.dto.response.PositionResponseDTO;
import com.employee.management.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class PositionController {

    private final PositionService positionService;

    // CREATE
    @PostMapping
    public ResponseEntity<PositionResponseDTO> createPosition(
            @Valid @RequestBody PositionRequestDTO requestDTO) {

        log.info("REST request to create position: {}", requestDTO.getTitle());
        PositionResponseDTO response = positionService.createPosition(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PositionResponseDTO> updatePosition(
            @PathVariable Long id,
            @Valid @RequestBody PositionRequestDTO requestDTO) {

        log.info("REST request to update position with ID: {}", id);
        PositionResponseDTO response = positionService.updatePosition(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        log.info("REST request to delete position with ID: {}", id);
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<PositionResponseDTO> getPositionById(@PathVariable Long id) {
        log.info("REST request to get position with ID: {}", id);
        PositionResponseDTO response = positionService.getPositionById(id);
        return ResponseEntity.ok(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<PositionResponseDTO>> getAllPositions() {
        log.info("REST request to get all positions");
        List<PositionResponseDTO> response = positionService.getAllPositions();
        return ResponseEntity.ok(response);
    }

    // GET BY DEPARTMENT
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<PositionResponseDTO>> getPositionsByDepartment(
            @PathVariable Long departmentId) {

        log.info("REST request to get positions for department ID: {}", departmentId);
        List<PositionResponseDTO> response = positionService.getPositionsByDepartment(departmentId);
        return ResponseEntity.ok(response);
    }
}