package ru.stepup;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.stepup.entity.User;
import ru.stepup.service.UserService;

@SpringBootApplication
public class Application {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() {
        // Create a new user
        User newUser = new User();
        newUser.setUsername("testUser");
        userService.save(newUser);

        // Find a user by ID
        User foundUser = userService.findById(newUser.getId());
        System.out.println("Found user: " + foundUser.getUsername());

        // Update a user
        foundUser.setUsername("updatedUser");
        userService.save(foundUser);

        // Delete a user
        userService.deleteById(foundUser.getId());
    }
}