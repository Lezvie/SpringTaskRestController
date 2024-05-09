package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.configs.PasswordEncoderConfig;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoderConfig passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoderConfig passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void create(User user) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User newUser) {
            User existingUser = userRepository.findById(id).orElse(null);
            existingUser.setUsername(newUser.getUsername());
            String encodedPassword = passwordEncoder.passwordEncoder().encode(newUser.getPassword());
            existingUser.setPassword(encodedPassword);
            existingUser.setRoles(newUser.getRoles());
            userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Long id) {
            return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
            userRepository.deleteById(id);
    }

    @Override
    public List<User> getList() {
        return userRepository.findAll();
    }


    @Override
    public User getUserAndRoles(User user, String[] roles) {
        user.setRoles(roleService.getRoleByName(roles));
        return user;
    }


    @Override
    public List<User> getUserByUsername(String name) {
        List<User> list = new ArrayList<>();
        list.add(userRepository.findByUsername(name));
        return list;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userRepository.findByUsername(username);
        if (username == null) {
            throw new UsernameNotFoundException("User с именем " + username + " не был найден!");
        }
        return userDetails;
    }
}