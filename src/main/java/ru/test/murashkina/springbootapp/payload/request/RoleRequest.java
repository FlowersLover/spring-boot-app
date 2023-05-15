package ru.test.murashkina.springbootapp.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoleRequest {//для записи ролей в бд
    private String name;
}
