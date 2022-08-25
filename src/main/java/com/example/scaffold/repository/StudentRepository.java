package com.example.scaffold.repository;

import com.example.scaffold.model.jpa.oneToMany.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
