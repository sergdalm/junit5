package com.dmdev.unit.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.service.UserService;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({
        MockitoExtension.class
})
public class UserServiceTest {
    private final static User IVAN = new User(1, "Ivan", LocalDate.of(1993, 7, 12), "ivan@mail.com", "Ivan", Role.USER, Gender.MALE);
    private final static User IVAN_WITHOUT_ID = new User(null, "Ivan", LocalDate.of(1993, 7, 12), "ivan@mail.com", "Ivan", Role.USER, Gender.MALE);
    private final static CreateUserDto IVAN_CREATE_USER_DTO = CreateUserDto.builder()
            .email(IVAN.getEmail())
            .name(IVAN.getName())
            .birthday(IVAN.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .password(IVAN.getPassword())
            .gender(IVAN.getGender().name())
            .role(IVAN.getRole().name())
            .build();
    private final static CreateUserDto INVALID_CREATE_USER_DTO = CreateUserDto.builder()
            .email(IVAN.getEmail())
            .name(IVAN.getName())
            .birthday("11111111111")
            .password(IVAN.getPassword())
            .gender("ANOTHER")
            .role("SOLDER")
            .build();
    private final static UserDto IVAN_DTO_WITHOUT_ID = UserDto.builder()
            .id(null)
            .name(IVAN.getName())
            .email(IVAN.getEmail())
            .gender(IVAN.getGender())
            .birthday(IVAN.getBirthday())
            .image("imageAddress")
            .build();
    private final static UserDto IVAN_DTO = UserDto.builder()
            .id(IVAN.getId())
            .name(IVAN.getName())
            .email(IVAN.getEmail())
            .gender(IVAN.getGender())
            .birthday(IVAN.getBirthday())
            .image("imageAddress")
            .build();
    private static ValidationResult VALIDATION_RESULT;

    @Mock
    private UserDao mockUserDao;
    @Mock
    private CreateUserMapper mockCreateUserMapper;
    @Mock
    private UserMapper mockUserMapper;
    @Mock
    CreateUserValidator mockCreateUserValidator;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void initService() {
        VALIDATION_RESULT = new ValidationResult();
    }

    @Test
    void shouldCreateNewUser() {
        when(mockUserDao.save(IVAN_WITHOUT_ID)).thenReturn(IVAN);
        when(mockCreateUserMapper.map(IVAN_CREATE_USER_DTO)).thenReturn(IVAN_WITHOUT_ID);
        when(mockUserMapper.map(IVAN_WITHOUT_ID)).thenReturn(IVAN_DTO_WITHOUT_ID);
        when(mockCreateUserValidator.validate(IVAN_CREATE_USER_DTO)).thenReturn(VALIDATION_RESULT);

        UserDto actualSavedUserDto = userService.create(IVAN_CREATE_USER_DTO);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(mockUserDao).save(userArgumentCaptor.capture());
        User userArgumentCaptorValue = userArgumentCaptor.getValue();

        assertEquals(IVAN_WITHOUT_ID, userArgumentCaptorValue);
        assertEquals(IVAN_DTO_WITHOUT_ID, actualSavedUserDto);
    }

    @Test
    void shouldThrowExceptionIfValidationFailed() {
        VALIDATION_RESULT.add(Error.of("invalid.birthday", "Birthday is invalid"));
        when(mockCreateUserValidator.validate(INVALID_CREATE_USER_DTO)).thenReturn(VALIDATION_RESULT);

        assertThrows(ValidationException.class, () -> userService.create(INVALID_CREATE_USER_DTO));
    }

    @Test
    void shouldLoginExistingUser() {
        when(mockUserDao.findByEmailAndPassword(IVAN.getEmail(), IVAN.getPassword())).thenReturn(Optional.of(IVAN));
        when(mockUserMapper.map(IVAN)).thenReturn(IVAN_DTO);

        Optional<UserDto> optionalUserDto = userService.login(IVAN.getEmail(), IVAN.getPassword());

        ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockUserDao).findByEmailAndPassword(emailArgumentCaptor.capture(), passwordArgumentCaptor.capture());
        String emailArgumentCaptorValue = emailArgumentCaptor.getValue();
        String passwordArgumentCaptorValue = passwordArgumentCaptor.getValue();

        assertEquals(IVAN.getEmail(), emailArgumentCaptorValue);
        assertEquals(IVAN.getPassword(), passwordArgumentCaptorValue);
        assertEquals(Optional.of(IVAN_DTO), optionalUserDto);
    }
}
