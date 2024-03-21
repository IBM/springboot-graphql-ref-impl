package com.ibm.gqldemo.controllers;

import com.ibm.gqldemo.model.Department;
import com.ibm.gqldemo.model.Employee;
import com.ibm.gqldemo.repositories.DepartmentRepository;
import com.ibm.gqldemo.service.EmployeeService;
import com.ibm.gqldemo.service.EmployeeSubscription;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@AllArgsConstructor
public class EmployeeController {

    private DepartmentRepository deptRepo;

    private EmployeeService employeeService;

    private EmployeeSubscription employeeSubscription;

    @QueryMapping("allEmployees")
    public List<Employee> allEmployees() {

        log.info("Searching all employees");
        return employeeService.findAll();
    }

    @MutationMapping("createEmployee")
    public Employee createEmployee(@Argument Employee employee) {
        employeeService.save(employee);
        return employee;
    }

    @BatchMapping(typeName = "Employee", field = "department")
    public Mono<Map<Employee, Department>> loadDepartments(Set<Employee> employeeSet) {
        var deptsIds = employeeSet.stream().map(Employee::getDeptId).collect(Collectors.toSet());
        var depts = deptRepo.findAllById(deptsIds);
        System.out.println("loading depts");
        log.info("Loading departments");
        return Mono.just(employeeSet.stream()
                .collect(Collectors.toMap(
                        emp -> emp,
                        emp -> depts.stream()
                                .filter( dept -> dept.getId() == emp.getDeptId())
                                .findFirst()
                                .orElse(new Department())
                )));
    }

    @SubscriptionMapping(name = "employeeSubscription")
    public Flux<Employee> employeeSubscription() {
        log.info("Starting Employee subscription");
        return employeeSubscription.employees();
    }

}
