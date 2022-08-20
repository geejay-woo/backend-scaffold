package com.example.scaffold.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Table
@Entity
@Accessors(chain = true)
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy="school", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Student> students;
}

