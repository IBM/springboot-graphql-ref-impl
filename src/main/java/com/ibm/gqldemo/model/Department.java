package com.ibm.gqldemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
public class Department {
    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String name;
}
