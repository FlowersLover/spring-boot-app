package ru.test.murashkina.springbootapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @NotBlank
    @Size(max = 20)
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
    @Size(max = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private Date createdAt;

    public User(String name, String familyName, String middleName, String email, String password, Status status, Role role, Date createdAt) {
        this.name = name;
        this.familyName = familyName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(String username, String email, String password) {
        this.name = username;
        this.email = email;
        this.password = password;
    }
}
