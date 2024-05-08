package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.configs.PasswordEncoderConfig;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;
import ru.itmentor.spring.boot_security.demo.util.CreateUserException;
import ru.itmentor.spring.boot_security.demo.util.GetUserException;
import ru.itmentor.spring.boot_security.demo.util.UpdateUserException;
import ru.itmentor.spring.boot_security.demo.util.UserNotFoundException;

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
        try {
            if (user.getUsername().equals("") | user.getPassword().equals("")) {
                throw new UsernameNotFoundException("User не имеет пароля и логина!");
            } else {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                userRepository.save(user);
            }
        }catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("User не имеет пароля и логина!");
        }catch (Exception e){
            throw new CreateUserException();
        }
    }

    @Override
    @Transactional
    public void updateUser(Long id, User newUser) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException());
            existingUser.setUsername(newUser.getUsername());
            String encodedPassword = passwordEncoder.passwordEncoder().encode(newUser.getPassword());
            existingUser.setPassword(encodedPassword);
            existingUser.setRoles(newUser.getRoles());
            userRepository.save(existingUser);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            throw new UpdateUserException();
        }
    }

    @Override
    public User getUserById(Long id) {
        try{
            return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
        }catch (UserNotFoundException e){
            throw new UserNotFoundException();
        }catch (Exception e){
            throw new GetUserException();
        }

    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new UserNotFoundException();
        }
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