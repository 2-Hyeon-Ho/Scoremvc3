package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/students")
public class StudentRestController {

    private final StudentRepository studentRepository;

    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable("studentId") Long studentId,
                              Model model) {
        Student student = studentRepository.getStudent(studentId);
        model.addAttribute("id", student);
        if (Objects.isNull(student)) {
            throw new StudentNotFoundException();
        }
        return student;
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody StudentRegisterRequest student,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        Student registrationStudent = studentRepository.register(student.getName(),
                student.getEmail(), student.getScore(), student.getComment());

        return ResponseEntity.created(URI.create("/students")).body(registrationStudent);
    }

    @PutMapping("/{studentId}/modify")
    public ResponseEntity<Student> modifyStudent(@PathVariable("studentId") Long studentId,
                                                 @Valid @RequestBody StudentModifyRequest requestStudent,
                                                 BindingResult bindingResult) {

        Student student = studentRepository.getStudent(studentId);

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        student.setName(requestStudent.getName());
        student.setEmail(requestStudent.getEmail());
        student.setScore(requestStudent.getScore());
        student.setComment(requestStudent.getComment());

        studentRepository.modify(student);

        return ResponseEntity.ok(student);
    }
}
