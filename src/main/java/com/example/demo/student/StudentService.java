package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentReposiory studentReposiory;

    @Autowired
    public StudentService(StudentReposiory studentReposiory) {
        this.studentReposiory = studentReposiory;
    }

    public List<Student> getStudents() {
        return studentReposiory.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentReposiory
                .findByStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentReposiory.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentReposiory.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("student with id " + studentId + " does not exist");
        }
        studentReposiory.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId,
                              String name,
                              String email) {
       Student student = studentReposiory.findById(studentId)
               .orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exist"));

       if (name != null &&
               !name.isEmpty() &&
               !Objects.equals(student.getName(), name)) {
           student.setName(name);
       }

       if (email != null &&
            !email.isEmpty() &&
            !Objects.equals(student.getEmail(), email)) {
           Optional<Student> studentOptional = studentReposiory
                   .findByStudentByEmail(student.getEmail());
           if (studentOptional.isPresent()) {
               throw new IllegalStateException("email taken");
           }

           studentReposiory.save(student);
       }
    }
}
