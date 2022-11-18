package com.nhnacademy.springmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.domain.StudentRegisterRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentRestControllerTest {
    private MockMvc mockMvc;
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new StudentRestController(studentRepository))
                .defaultRequest(get("/students").accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8))
                .build();
    }

    @Test
    @DisplayName("존재하지 않는 학생 조회시 예외처리")
    void notExist_student_test() {
        when(studentRepository.getStudent(10)).thenReturn(null);

        Long studentId = 10L;
        Throwable th = catchThrowable(() -> mockMvc.perform(get("/students/{studentId}", studentId)));

        assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(StudentNotFoundException.class);
    }

    @Test
    @DisplayName("학생을 조회했을 때 제대로 된 값이 조회되는지 테스트")
    void exist_student_test() throws Exception {
        Student student = Student.create(2L, "abc", "abc@def", 50, "good");
        when(studentRepository.getStudent(2L)).thenReturn(student);

        Long studentId = 2L;
        mockMvc.perform(get("/students/{studentId}", studentId))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("name").value("abc"))
                .andExpect(jsonPath("email").value("abc@def"))
                .andExpect(jsonPath("score").value(50))
                .andExpect(jsonPath("comment").value("good"))
                .andReturn();

        verify(studentRepository, times(1)).getStudent(anyLong());
    }

    @Test
    @DisplayName("학생을 등록할 때 적절한 값이 들어가지 않았을 때 예외처리")
    void validation_fail_registerStudent_test() {
        //given
        StudentRegisterRequest student = new StudentRegisterRequest("abc", "123", 110, "good");
        ObjectMapper objectMapper = new ObjectMapper();

        Throwable th = catchThrowable(() -> mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(student)
                        ))
                .andDo(print()));

        assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(ValidationFailedException.class);
    }

    @Test
    @DisplayName("학생 등록이 제대로 되는지 테스트")
    void validation_registerStudent_test() throws Exception {
        Student student = Student.create(2L, "abc", "abc@abc", 80, "good");
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(student)
                        ))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(studentRepository, times(1)).register("abc", "abc@abc", 80, "good");
    }

    @Test
    @DisplayName("학생 정보 수정시 제대로 수정이 됐는지 테스트")
    void modifyStudent_test() throws Exception {
        Student student = Student.create(2L, "abc", "abc@abc", 80, "good");
        ObjectMapper objectMapper = new ObjectMapper();

        String modifyName = "modifyStudent";
        String modifyEmail = "new@abc";
        int modifyScore = 10;
        String modifyComment = "bad";
        Student modifyStudent = Student.create(student.getId(), modifyName, modifyEmail, modifyScore, modifyComment);

        Long studentId = 2L;
        when(studentRepository.getStudent(studentId)).thenReturn(student);

        ResultActions mvcResult = mockMvc.perform(put("/students/{studentId}/modify", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(modifyStudent)
                        ))
                .andDo(print())
                .andExpect(status().isOk());

        mvcResult
                .andExpect(jsonPath("name").value("modifyStudent"))
                .andExpect(jsonPath("email").value("new@abc"))
                .andExpect(jsonPath("score").value(10))
                .andExpect(jsonPath("comment").value("bad"));
    }

    @Test
    @DisplayName("학생 정보 수정시 적절한 값이 들어가지 않을 때 예외처리")
    void validation_fail_modify_student_test() {
        StudentModifyRequest student = new StudentModifyRequest("abc", "123", 110, "good");
        ObjectMapper objectMapper = new ObjectMapper();

        Long studentId = 1L;
        Throwable th = catchThrowable(() -> mockMvc.perform(put("/students/{studentId}/modify", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(student)
                )));

        assertThat(th).isInstanceOf(NestedServletException.class)
                .hasCauseInstanceOf(ValidationFailedException.class);
    }
}