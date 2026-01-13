package com.employee.management.service.impl;

import com.employee.management.dto.request.PositionRequestDTO;
import com.employee.management.dto.response.PositionResponseDTO;
import com.employee.management.entity.Department;
import com.employee.management.entity.Position;
import com.employee.management.repository.DepartmentRepository;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.PositionRepository;
import com.employee.management.service.PositionService;
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
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public PositionResponseDTO createPosition(PositionRequestDTO requestDTO) {
        log.info("Creating new position: {}", requestDTO.getTitle());

        // Check if department exists
        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + requestDTO.getDepartmentId()));

        // Check if position title already exists in this department
        if (positionRepository.existsByTitleAndDepartmentId(requestDTO.getTitle(), requestDTO.getDepartmentId())) {
            throw new RuntimeException("Position with title '" + requestDTO.getTitle() +
                    "' already exists in this department");
        }

        // Create entity
        Position position = new Position();
        position.setTitle(requestDTO.getTitle());
        position.setDescription(requestDTO.getDescription());
        position.setDepartment(department);

        // Save
        Position savedPosition = positionRepository.save(position);

        log.info("Position created successfully with ID: {}", savedPosition.getId());
        return convertToResponseDTO(savedPosition);
    }

    @Override
    public PositionResponseDTO updatePosition(Long id, PositionRequestDTO requestDTO) {
        log.info("Updating position with ID: {}", id);

        // Find existing position
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with ID: " + id));

        // Check if department exists (if changing department)
        if (!position.getDepartment().getId().equals(requestDTO.getDepartmentId())) {
            Department newDepartment = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with ID: " + requestDTO.getDepartmentId()));
            position.setDepartment(newDepartment);
        }

        // Check for title conflicts
        if (!position.getTitle().equals(requestDTO.getTitle()) &&
                positionRepository.existsByTitleAndDepartmentId(requestDTO.getTitle(), requestDTO.getDepartmentId())) {
            throw new RuntimeException("Position with title '" + requestDTO.getTitle() +
                    "' already exists in this department");
        }

        // Update fields
        position.setTitle(requestDTO.getTitle());
        position.setDescription(requestDTO.getDescription());

        // Save
        Position updatedPosition = positionRepository.save(position);

        log.info("Position updated successfully");
        return convertToResponseDTO(updatedPosition);
    }

    @Override
    public void deletePosition(Long id) {
        log.info("Deleting position with ID: {}", id);

        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with ID: " + id));

        // Check if position has employees
        long employeeCount = employeeRepository.countByPositionId(id);
        if (employeeCount > 0) {
            throw new RuntimeException("Cannot delete position with " + employeeCount + " employees. " +
                    "Please reassign employees first.");
        }

        positionRepository.delete(position);

        log.info("Position deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public PositionResponseDTO getPositionById(Long id) {
        log.info("Fetching position with ID: {}", id);

        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with ID: " + id));

        return convertToResponseDTO(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionResponseDTO> getAllPositions() {
        log.info("Fetching all positions");

        return positionRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionResponseDTO> getPositionsByDepartment(Long departmentId) {
        log.info("Fetching positions for department ID: {}", departmentId);

        // Check if department exists
        if (!departmentRepository.existsById(departmentId)) {
            throw new RuntimeException("Department not found with ID: " + departmentId);
        }

        return positionRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method
    private PositionResponseDTO convertToResponseDTO(Position position) {
        PositionResponseDTO dto = new PositionResponseDTO();
        dto.setId(position.getId());
        dto.setTitle(position.getTitle());
        dto.setDescription(position.getDescription());
        dto.setDepartmentId(position.getDepartment().getId());
        dto.setDepartmentName(position.getDepartment().getName());
        dto.setEmployeeCount(employeeRepository.countByPositionId(position.getId()).intValue());
        dto.setCreatedAt(position.getCreatedAt());
        dto.setUpdatedAt(position.getUpdatedAt());
        return dto;
    }
}