package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.User;
import com.epam.training.ticketservice.core.user.persistance.UserRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

public class UserServiceImplTest {

    User user = new User("admin", "admin", User.Role.ADMIN);

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserServiceImpl underTest = new UserServiceImpl(userRepository);

    @Test
    void testLoginShouldSetLoggedInUserWhenUsernameAndPasswordAreCorrect() {
        when(userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.of(user));

        Optional<UserDto> actual = underTest.login(user.getUsername(), user.getPassword());

        assertEquals(user.getUsername(), actual.get().username());
        assertEquals(user.getRole(), actual.get().role());
        verify(userRepository).findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Test
    void testLoginShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        when(userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.empty());

        Optional<UserDto> actual = underTest.login(user.getUsername(), user.getPassword());

        assertEquals(Optional.empty(), actual);
        verify(userRepository).findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Test
    void testLogoutShouldReturnOptionalEmptyWhenUserIsNotLoggedIn() {
        Optional<UserDto> expected = Optional.empty();

        Optional<UserDto> actual = underTest.logout();

        assertEquals(expected, actual);
    }

    @Test
    void testLogoutShouldReturnThePreviouslyLoggedInUserWhenUserIsLoggedIn() {
        when(userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(Optional.of(user));
        Optional<UserDto> expected = underTest.login("user", "us");

        Optional<UserDto> actual = underTest.logout();

        assertEquals(expected, actual);
    }
}
