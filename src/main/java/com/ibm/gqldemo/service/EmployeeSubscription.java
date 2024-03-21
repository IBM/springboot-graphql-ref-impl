package com.ibm.gqldemo.service;

import com.ibm.gqldemo.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class EmployeeSubscription {

    Sinks.Many<Employee> employeeSink;

    public EmployeeSubscription() {
        createEmployeeSink();
    }

    public Flux<Employee> employees() {
        return employeeSink.asFlux().doFinally(this::clearSinkIfNoSubscribers);
    }

    private void clearSinkIfNoSubscribers(SignalType signalType) {
        if(this.employeeSink.currentSubscriberCount() == 0) {
            employeeSink = null;
        }
    }

    public Flux<Employee> employee(Integer deptId) {
        return employeeSink.asFlux()
                .filter(employee -> deptId.equals(employee.getDeptId()))
                .doFinally(this::clearSinkIfNoSubscribers);
    }

    public void publish(Employee employee) {
        if(employeeSink == null) {
            createEmployeeSink();
        }
        log.info("Publishing employee to subscription: {}", employee);
        employeeSink.tryEmitNext(employee);
    }

    private void createEmployeeSink() {
        log.info("Initializing subscription sink");
        employeeSink = Sinks.many().multicast().directBestEffort();
    }
}
