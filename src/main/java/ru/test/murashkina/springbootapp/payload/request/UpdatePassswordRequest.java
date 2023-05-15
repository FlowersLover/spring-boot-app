package ru.test.murashkina.springbootapp.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePassswordRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
}
