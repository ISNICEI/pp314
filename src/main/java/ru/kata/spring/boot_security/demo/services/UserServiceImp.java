package ru.kata.spring.boot_security.demo.services;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAllWithRoles();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> Hibernate.initialize(u.getRoles())); // ← принудительная инициализация
        return user;
    }

    @Override
    @Transactional
    public void save(User user, Set<Role> roles) {
        prepareUserForSave(user, roles);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void update(User user, Set<Role> roles, Long id) {
        userRepository.findById(id).ifPresent(u -> {
            updateUserFields(u, user, roles);
            userRepository.save(u);
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElse(null);
        Hibernate.initialize(user.getRoles());
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Role getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                User user = userRepository.findByUsernameWithRoles(username);
                Hibernate.initialize(user.getRoles());
                return user;
            }
        }
        throw new IllegalStateException("No authenticated user found");
    }

    private void prepareUserForSave(User user, Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
    }

    private void updateUserFields(User user, User updatedUser, Set<Role> roles) {
        user.setUsername(updatedUser.getUsername());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null
                && !updatedUser.getPassword().isEmpty()
                && !passwordEncoder.matches(updatedUser.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (roles != null && !roles.isEmpty()) {
            user.setRoles(roles);
        }
    }
}
