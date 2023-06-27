package com.example.scaffold.model.master;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`project_member`")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userId;

    @Column(name = "projectCode", insertable = false, updatable = false)
    private String projectCode;

    @JoinColumn(name = "projectCode", referencedColumnName = "projectCode")
    @OneToOne
    private Project project;

    private String roleName;
}
