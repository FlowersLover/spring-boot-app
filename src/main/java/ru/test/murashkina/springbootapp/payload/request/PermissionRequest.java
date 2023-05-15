package ru.test.murashkina.springbootapp.payload.request;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PermissionRequest {
    @Id
    private String name;
    private String description;
}
