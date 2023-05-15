package ru.test.murashkina.springbootapp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoleResponse {

    private String id;

    private String name;

    private String description;
}
