package ru.kata.spring.boot_security.demo.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "user")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  @NotNull(message = "Недопустимое имя")
  private String firstName;

  @Column(name = "last_name")
  @NotBlank(message = "Недопустимая фамилия")
  private String lastName;

  @Column(name = "email")
  @NotBlank(message = "Недопустимаый email")
  @Email
  private String email;

  @NotBlank(message = "Пароль не может быть пустым")
  @Column(name = "password")
  private String password;

  @NotBlank(message = "Имя пользователя не может быть пустым")
  @Column(unique = true)
  private String username;

  @JsonManagedReference
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  @Override
  @Transactional
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.getRoles().stream()
            .map(role -> {
              String roleName = role.getName();
              if (!roleName.startsWith("ROLE_")) {
                roleName = "ROLE_" + roleName;
              }
              return new SimpleGrantedAuthority(roleName);
            })
            .collect(Collectors.toList());
  }

  public User() {}


  public User(String firstName, String lastName, String email, String password, String username) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;

    this.password = password;
    this.username = username;
  }
  public String getAllUserRoles() {
    return this.roles.stream()
            .map(Role::getName)
            .collect(Collectors.joining(", "));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUsername(String username) {
    this.username = username;
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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName)
            && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email)
            && Objects.equals(password, user.password) && Objects.equals(username, user.username)
            && Objects.equals(roles, user.roles);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(id);
    result = 31 * result + Objects.hashCode(firstName);
    result = 31 * result + Objects.hashCode(lastName);
    result = 31 * result + Objects.hashCode(email);
    result = 31 * result + Objects.hashCode(password);
    result = 31 * result + Objects.hashCode(username);
    result = 31 * result + Objects.hashCode(roles);
    return result;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", username='" + username + '\'' +
            ", roles=" + roles +
            '}';
  }
}
