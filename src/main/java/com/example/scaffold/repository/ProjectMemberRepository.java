package com.example.scaffold.repository;

import com.example.scaffold.model.master.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {
}
