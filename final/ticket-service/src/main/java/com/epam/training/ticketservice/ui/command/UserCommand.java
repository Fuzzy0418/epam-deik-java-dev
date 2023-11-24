package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class UserCommand {

    private final UserService userService;

    @ShellMethod(key = "sign out", value = "User logout")
    public String logout() {
        Optional<UserDto> user = userService.logout();
        if (user.isPresent()) {
            return "Signed out with privileged account '" + user.get().username() + "'";
        } else {
            return "You are not signed in";
        }
    }

    @ShellMethod(key = "sign in privileged", value = "User login")
    public String login(String username, String password) {
        if (userService.login(username, password).isPresent()) {
            return "Signed in with privileged account '" + username + "'";
        } else {
            return "Login failed due to incorrect credentials";
        }
    }

    @ShellMethod(key = "describe account", value = "Get user information")
    public String describe() {
        Optional<UserDto> user = userService.describe();
        if (user.isPresent()) {
            return "Signed in with privileged account '" + user.get().username() + "'";
        } else {
            return "You are not signed in";
        }
    }

}
