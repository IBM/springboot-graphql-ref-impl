package com.ibm.gqldemo.repositories;

import com.ibm.gqldemo.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
