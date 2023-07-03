package com.example.customuser2.entity.user;

import com.example.customuser2.domain.type.UseYN;
import com.example.customuser2.domain.type.UserGrade;
import com.example.customuser2.domain.type.converter.UserGradeAttributeConverter;
import com.example.customuser2.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Convert(converter = UserGradeAttributeConverter.class)
    @Column(name = "user_grade", nullable = false, length = 20)
    private UserGrade userGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "enabled", nullable = false)
    private UseYN enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

    public User(String email, String password, UserGrade userGrade, UseYN enabled) {
        this.email = email;
        this.password = password;
        this.userGrade = userGrade;
        this.enabled = enabled;
    }

    public User(String email, String password, UserGrade userGrade, UseYN enabled, Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.userGrade = userGrade;
        this.enabled = enabled;
        this.roles = roles;
    }

    public void addRole(Collection<Role> roles) {
        this.roles.addAll(roles);
    }
}
