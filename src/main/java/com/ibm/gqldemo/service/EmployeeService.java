package com.ibm.gqldemo.service;

import com.ibm.gqldemo.model.Employee;
import com.ibm.gqldemo.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private EmployeeSubscription employeeSubscription;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeSubscription employeeSubscription) {
        this.employeeRepository = employeeRepository;
        this.employeeSubscription = employeeSubscription;
    }

    public void save(Employee employee) {
        employeeRepository.save(employee);
        employeeSubscription.publish(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
