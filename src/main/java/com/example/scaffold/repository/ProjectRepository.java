package com.example.scaffold.repository;

import com.example.scaffold.model.master.OrderTemplateMapping;
import com.example.scaffold.model.master.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {
}
