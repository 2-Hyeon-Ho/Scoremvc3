package com.nhnacademy.springmvc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentModifyRequest {
    @JsonProperty("name")
    @NotBlank
    String name;

    @JsonProperty("email")
    @Email
    String email;

    @JsonProperty("score")
    @Max(100) @Min(0)
    int score;

    @JsonProperty("comment")
    @NotBlank@Size(max = 200)
    String comment;
}
