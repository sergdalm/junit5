package com.dmdev.integration.srvice;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceIT extends IntegrationTestBase {
    private final static User IVAN = new User(1, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", Role.ADMIN, Gender.MALE);
    private final static CreateUserDto CREATE_USER_DTO = CreateUserDto.builder()
            .email("kolya@gmai.com")
            .name("Kolya")
            .birthday("1993-07-12")
            .password("1234")
            .gender("MALE")
            .role("USER")
            .build();

    private final UserService userService = new UserService();

    @Test
    void shouldInitializeUserService() {
        UserService userService = new UserService();

        assertThat(userService).isNotNull();
    }

    @Test
    void shouldCreateNewUser() {
        UserDto userDtoResult = userService.create(CREATE_USER_DTO);

        assertThat(userDtoResult).isNotNull();
        assertEquals(CREATE_USER_DTO.getName(), userDtoResult.getName());
        assertEquals(CREATE_USER_DTO.getEmail(), userDtoResult.getEmail());
        assertEquals(CREATE_USER_DTO.getRole(), userDtoResult.getRole().name());
        assertEquals(CREATE_USER_DTO.getGender(), userDtoResult.getGender().name());
    }

    @Test
    void shouldLoginExitingUser() {
        Optional<UserDto> userDtoOptional = userService.login(IVAN.getEmail(), IVAN.getPassword());

        assertThat(userDtoOptional).isNotEmpty();
        assertEquals(IVAN.getId(), userDtoOptional.get().getId());
        assertEquals(IVAN.getName(), userDtoOptional.get().getName());
        assertEquals(IVAN.getGender(), userDtoOptional.get().getGender());
        assertEquals(IVAN.getRole(), userDtoOptional.get().getRole());
        assertEquals(IVAN.getEmail(), userDtoOptional.get().getEmail());
        assertEquals(IVAN.getBirthday(), userDtoOptional.get().getBirthday());
    }
}
