package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.Valid;
import java.util.Objects;


@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @ModelAttribute("student")
    public Student getStudent(@PathVariable("studentId") Long studentId) {
        Student student = studentRepository.getStudent(studentId);
        if (Objects.isNull(student)) {

            throw new StudentNotFoundException();
        }
        return student;
    }

    @GetMapping("/{studentId}")
    public String viewStudent(@ModelAttribute Student student,
                              ModelMap modelMap) {
        modelMap.put("student", student);
        return "thymeleaf/studentView";
    }

    @GetMapping(path = "{studentId}",params = { "hideScore=yes" })
    public String hideViewStudent(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("student", student);
        return "thymeleaf/hideScoreView";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm(@ModelAttribute("student") Student student,
                                    Model model) {
        model.addAttribute("student", student);
        return "thymeleaf/studentModify";
    }

    @PostMapping("/{studentId}/modify")
    public String modifyStudent(@ModelAttribute Student student,
                             @PathVariable("studentId") Long studentId,
                             @Valid @ModelAttribute StudentModifyRequest studentRequest,
                             BindingResult bindingResult,
                             Model model) {
        if (Objects.isNull(student)) {
            model.addAttribute("exception", new StudentNotFoundException());
            return "thymeleaf/error";
        }
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        student.setName(studentRequest.getName());
        student.setEmail(studentRequest.getEmail());
        student.setScore(studentRequest.getScore());
        student.setComment(studentRequest.getComment());

        studentRepository.modify(student);

        model.addAttribute("student", student);
        return "thymeleaf/studentView";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404Exception(Exception ex, Model model) {
        model.addAttribute("exception", ex);
        return "thymeleaf/error";
    }
}
