package com.employee.management.service;

import com.employee.management.dto.request.PositionRequestDTO;
import com.employee.management.dto.response.PositionResponseDTO;

import java.util.List;

public interface PositionService {

    PositionResponseDTO createPosition(PositionRequestDTO requestDTO);

    PositionResponseDTO updatePosition(Long id, PositionRequestDTO requestDTO);

    void deletePosition(Long id);

    PositionResponseDTO getPositionById(Long id);

    List<PositionResponseDTO> getAllPositions();

    List<PositionResponseDTO> getPositionsByDepartment(Long departmentId);
}