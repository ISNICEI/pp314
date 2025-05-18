package ru.kata.spring.boot_security.demo.util;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class InitializationTable {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    public InitializationTable(UserRepository userRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializationTable() {
        String passwordAdmin = passwordEncoder.encode("admin");
        String passwordUser = passwordEncoder.encode("user");
        User admin = new User(
                "Test",
                "Testov",
                "admin@mail.ru",
                passwordAdmin,
                "admin");
        User user = new User(
                "Test",
                "Testov",
                "user@mail.ru",
                passwordUser,
                "user");

        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        Set<Role> setAdmin = new HashSet<>();
        Set<Role> setUser = new HashSet<>();

        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);

        setAdmin.add(roleAdmin);
        setAdmin.add(roleUser);
        admin.setRoles(setAdmin);
        userRepository.save(admin);

        setUser.add(roleUser);
        user.setRoles(setUser);
        userRepository.save(user);
    }

}