package ru.test.murashkina.springbootapp.payload.response.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrResponse {
    private String message;
    private String code;
    private String rejectedValue;
}
