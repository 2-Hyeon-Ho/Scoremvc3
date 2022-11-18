package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/student/register")
public class StudentRegisterController {
    private final StudentRepository studentRepository;

    public StudentRegisterController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String studentRegisterForm() {
        return "thymeleaf/studentRegister";
    }

    @PostMapping
    public ModelAndView registerStudent(@Valid @ModelAttribute StudentRegisterRequest studentRequest,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        Student student = studentRepository.register(studentRequest.getName(),
                studentRequest.getEmail(), studentRequest.getScore(), studentRequest.getComment());

        ModelAndView mav = new ModelAndView("thymeleaf/studentView");
        mav.addObject("student", student);

        return mav;
    }

}
