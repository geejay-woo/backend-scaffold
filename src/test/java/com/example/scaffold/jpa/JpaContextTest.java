package com.example.scaffold.jpa;

import com.example.scaffold.model.School;
import com.example.scaffold.model.Student;
import com.example.scaffold.repository.SchoolRepository;
import com.example.scaffold.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaContextTest {
    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void contextLoads() {
        Student jackMa = new Student().setName("Jack Ma");
        Student jackChen = new Student().setName("Jack Chen");
        School school = new School().setName("湖畔大学");

        List<Student> students = new ArrayList<>();
        students.add(jackMa);
        students.add(jackChen);

        // school作为关系的被维护方，student作为关系的维护方
        // 如果school设置了List<Student>，并且Student里面没有主动设置School对象
        // Student实体会被保存，但是关联关系字段school_fk不会被更新
        // 如果是多对多的关系，操作关系的被维护方，关系表不会新增数据

//        jackMa.setSchool(school);
//        jackChen.setSchool(school);
        school.setStudents(students);

        schoolRepository.save(school);
    }

    @Test
    public void saveStudent() {
        Student student = new Student();
        student.setName("testStudent");
        School school = new School();
        school.setName("testSchool");
        student.setSchool(school);
        studentRepository.save(student);

    }

    @Test
    public void deleteById() {
        schoolRepository.deleteById(3);
    }

    @Test
    public void updateBy() {

    }
}

