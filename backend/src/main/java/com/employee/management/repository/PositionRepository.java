package com.employee.management.repository;

import com.employee.management.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    // Find all positions in a specific department
    List<Position> findByDepartmentId(Long departmentId);

    // Check if position title exists in a department
    boolean existsByTitleAndDepartmentId(String title, Long departmentId);
}