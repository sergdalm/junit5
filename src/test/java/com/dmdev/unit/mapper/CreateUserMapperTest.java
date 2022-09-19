package com.dmdev.unit.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateUserMapperTest {
    private final static CreateUserMapper CREATE_USER_MAPPER = CreateUserMapper.getInstance();

    @ParameterizedTest
    @MethodSource("getUserDtoAndEntity")
    void shouldMapDtoToEntity(CreateUserDto createUserDto, User user) {
        User userResult = CREATE_USER_MAPPER.map(createUserDto);
        assertEquals(user, userResult);
    }

    @ParameterizedTest
    @MethodSource("getInvalidDto")
    void shouldThrowExceptionIfDtoFiledIsInvalid(CreateUserDto createUserDto) {
        assertThrows(Exception.class, () -> CREATE_USER_MAPPER.map(createUserDto));
    }

    static Stream<Arguments> getUserDtoAndEntity() {
        return Stream.of(
                Arguments.of(CreateUserDto.builder()
                                .name("Ivan")
                                .birthday("1990-01-10")
                                .email("ivan@gmail.com")
                                .password("111")
                                .role("ADMIN")
                                .gender("MALE")
                                .build(),
                        new User(null, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", Role.ADMIN, Gender.MALE)),
                Arguments.of(
                        CreateUserDto.builder()
                                .name("Petr")
                                .birthday("1995-10-19")
                                .email("petr@gmail.com")
                                .password("321")
                                .role("USER")
                                .gender("MALE")
                                .build(),
                        new User(null, "Petr", LocalDate.of(1995, 10, 19), "petr@gmail.com", "321", Role.USER, Gender.MALE)),
                Arguments.of(
                        CreateUserDto.builder()
                                .name("Sveta")
                                .birthday("2001-12-23")
                                .email("sveta@gmail.com")
                                .password("321")
                                .role("USER")
                                .gender("FEMALE")
                                .build(),
                        new User(null, "Sveta", LocalDate.of(2001, 12, 23), "sveta@gmail.com", "321", Role.USER, Gender.FEMALE)),
                Arguments.of(CreateUserDto.builder()
                                .name("Vlad")
                                .birthday("1984-03-14")
                                .email("vlad@gmail.com")
                                .password("456")
                                .role("USER")
                                .gender("MALE")
                                .build(),
                        new User(null, "Vlad", LocalDate.of(1984, 3, 14), "vlad@gmail.com", "456", Role.USER, Gender.MALE)),
                Arguments.of(
                        CreateUserDto.builder()
                                .name("Kate")
                                .birthday("1989-08-09")
                                .email("kate@gmail.com")
                                .password("777")
                                .role("ADMIN")
                                .gender("FEMALE")
                                .build(),
                        new User(null, "Kate", LocalDate.of(1989, 8, 9), "kate@gmail.com", "777", Role.ADMIN, Gender.FEMALE)),
                Arguments.of(CreateUserDto.builder()
                                .name("Ivan")
                                .birthday("1990-01-10")
                                .email("ivan@gmail.com")
                                .password("111")
                                .role("STUDENT")
                                .gender("MALE")
                                .build(),
                        new User(null, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", null, Gender.MALE)),
                Arguments.of(CreateUserDto.builder()
                                .name("Ivan")
                                .birthday("1990-01-10")
                                .email("ivan@gmail.com")
                                .password("111")
                                .role("ADMIN")
                                .gender("ANOTHER")
                                .build(),
                        new User(null, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", Role.ADMIN, null))
        );
    }

    static Stream<Arguments> getInvalidDto() {
        return Stream.of(
                Arguments.of(CreateUserDto.builder()
                        .name("Ivan")
                        .birthday("000")
                        .email("ivan@gmail.com")
                        .password("111")
                        .role("ADMIN")
                        .gender("MALE")
                        .build())
        );
    }
}
