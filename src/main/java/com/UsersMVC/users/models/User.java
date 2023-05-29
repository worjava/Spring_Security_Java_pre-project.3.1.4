package com.UsersMVC.users.models;


import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "поле не должны быть пустым")
    @Size(max = 30, message = "превышено количество символов")
    @Column(name = "firstname")
    private String firstname;
    @NotEmpty(message = "поле не должны быть пустым")
    @Size(max = 30, message = "превышено количество символов")
    @Column(name = "lastname")
    private String lastname;

    @Min(value = 0, message = "Поле не может быть пустым")
    @Max(value = 30, message = "Максимальный возраст не более 30")
    @Column(name = "age")
    private int age;
    @NotEmpty(message = "Поле не должно быть пустым")
    @Email(message = "Некоректный email")
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @ManyToMany
    @JoinTable(
            name = "users_roles", //таблица связывания ролей и юзеров
            joinColumns = @JoinColumn(name = "user_id"), // значение таблицы users_roles
            inverseJoinColumns = @JoinColumn(name = "role_id")) // значение таблицы users_roles
    @Fetch(FetchMode.JOIN)//для немедленной загрузки связанных сущностей
    private List<Role> roles;

    public User(String firstname, String lastname, int age, String email, String password, List<Role> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
