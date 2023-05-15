package ru.test.murashkina.springbootapp.payload.response.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldsResponse {
    private ErrResponse email;
    private ErrResponse password;
    private ErrResponse passwordResponse;

    public FieldsResponse(ErrResponse email) {
        this.email = email;
    }

    public FieldsResponse(ErrResponse password, ErrResponse passwordResponse) {
        this.password = password;
        this.passwordResponse = passwordResponse;
    }
}
