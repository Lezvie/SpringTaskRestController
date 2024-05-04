package ru.itmentor.spring.boot_security.demo.restControllers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmentor.spring.boot_security.demo.model.User;

@RestController
@RequestMapping("/api/user")
public class RestUserController {


    @GetMapping()
    public User getAllUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
            User user = (User) principal;
            return user;
    }

}
