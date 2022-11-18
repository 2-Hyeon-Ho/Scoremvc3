package com.nhnacademy.springmvc.config;

import com.nhnacademy.springmvc.repository.AdminRepository;
import com.nhnacademy.springmvc.repository.StudentRepository;
import com.nhnacademy.springmvc.repository.StudentRepositoryImpl;
import com.nhnacademy.springmvc.Base;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(basePackageClasses = Base.class,
        excludeFilters = { @ComponentScan.Filter(Controller.class)})
public class RootConfig {
    @Bean
    public StudentRepository studentRepository() {
        StudentRepository studentRepository = new StudentRepositoryImpl();
        studentRepository.register("김학생", "kim.student@nhnacademy.com", 100, "훌륭");

        return studentRepository;
    }

    @Bean
    public AdminRepository adminRepository() {
        AdminRepository adminRepository = new AdminRepository();
        adminRepository.addAdmin();

        return adminRepository;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasename("message");

        return messageSource;
    }

}
