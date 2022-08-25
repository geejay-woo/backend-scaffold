package com.example.scaffold.repository;

import com.example.scaffold.model.jpa.oneToMany.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Integer> {
}
