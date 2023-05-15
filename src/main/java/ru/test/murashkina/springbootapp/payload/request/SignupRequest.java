package ru.test.murashkina.springbootapp.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-zА-я-]*")
    private String name;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-zА-я-]*")
    private String familyName;

    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-zА-я-]*")
    private String middleName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    private String role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
