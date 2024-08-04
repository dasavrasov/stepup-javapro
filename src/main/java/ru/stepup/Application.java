package ru.stepup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.stepup.entity.User;
import ru.stepup.service.UserService;

import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context= SpringApplication.run(Application.class, args);
        UserService userService = context.getBean(UserService.class);

        System.out.println("Create a new user 1");
        // Create a new user
        User newUser1 = new User("User1");
        userService.save(newUser1);

        System.out.println("Create a new user 2");
        User newUser2 = new User("User2");
        userService.save(newUser2);
        //Find all
        System.out.println("Find all");
        List<User> users = userService.findAll();
        users.forEach(user -> System.out.println("Id: "+user.getId()+" Username: " + user.getUsername()));

        System.out.println("Find a user by name="+newUser1.getUsername());
        // Find a user by ID
        User foundUser = userService.findByName(newUser1.getUsername());
        System.out.println("Found User1 name: " + foundUser.getUsername());

        System.out.println("Update user1");
        // Update user
        foundUser.setUsername("updatedUser1");
        userService.save(foundUser);
        users= userService.findAll();
        System.out.println("Find all after update User 1");
        users.forEach(user -> System.out.println("Id: "+user.getId()+" Username: " + user.getUsername()));

        // Delete user
        System.out.println("Delete foundUser");
        userService.deleteById(foundUser.getId());

        System.out.println("Find all after delete of User 2");
        //Find all after delete
        users = userService.findAll();
        users.forEach(user -> System.out.println("Id: "+user.getId()+" Username: " + user.getUsername()));

    }
}