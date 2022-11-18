package com.nhnacademy.springmvc.domain;


import lombok.*;


@Data
@NoArgsConstructor
public class Student {
    private Long id;

    private String name;

    private String email;

    private int score;

    private String comment;

    public Student(Long id, String name, String email, int score, String comment) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.score = score;
        this.comment = comment;
    }

    public static Student hideScore(Student student) {
        Student newUser = Student.create(student.getId(), student.getName(), student.getEmail(), student.getScore(), student.getComment());
        newUser.setName(student.getName());
        newUser.setEmail(student.getEmail());

        return newUser;
    }

    public static Student create(Long id, String name, String email, int score, String comment) {
        return new Student(id, name, email, score, comment);
    }

}
