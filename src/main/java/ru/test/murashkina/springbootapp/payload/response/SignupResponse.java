package ru.test.murashkina.springbootapp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class SignupResponse {
    private UUID id;
    private String email;
    private String familyName;
    private String name;
    private String middleName;
    private String role;
    private String status;
    private String createdAt;
}
