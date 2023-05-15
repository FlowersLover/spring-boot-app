package ru.test.murashkina.springbootapp.payload.response.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CodeResponse {
    private String code;
    private String message;
}
