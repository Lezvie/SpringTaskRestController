package ru.itmentor.spring.boot_security.demo.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAdminController {
    private final UserService userService;

    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUser() {
        return userService.getList();
    }
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        userService.create(user);
        return user;
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) {
        userService.create(user);
        return user;
    }
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    }
