package com.ibm.gqldemo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "ID")
    @EqualsAndHashCode.Include
    private int id;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "NAME")
    private String name;

    @Column(name="DEPT_ID")
    private Integer deptId;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
}
