package ru.kata.spring.boot_security.demo.services;


import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    List<User> listUsers();

    void save(User user, Set<Role> roles);

    User getUserById(Long id);

    void update(User user, Set<Role> roles, Long id);

    void delete(Long id);

    User getCurrentUser();

    Optional<User> findByEmail(String email);
}
