package com.dmdev.unit.mapper;

import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.mapper.UserMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserMapperTest {
    private final static UserMapper USER_MAPPER = UserMapper.getInstance();

    @ParameterizedTest
    @MethodSource("getUserEntityAndDto")
    void shouldMapUserEntityToUserDto(User user, UserDto userDto) {
        UserDto userDtoResult = USER_MAPPER.map(user);
        assertEquals(userDto, userDtoResult);
    }

    static Stream<Arguments> getUserEntityAndDto() {
        return Stream.of(
                Arguments.of(
                        new User(1, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", Role.ADMIN, Gender.MALE),
                        UserDto.builder()
                                .id(1)
                                .name("Ivan")
                                .birthday(LocalDate.of(1990, 1, 10))
                                .email("ivan@gmail.com")
                                .role(Role.ADMIN)
                                .gender(Gender.MALE)
                                .build()),
                Arguments.of(
                        new User(2, "Petr", LocalDate.of(1995, 10, 19), "petr@gmail.com", "123", Role.USER, Gender.MALE),
                        UserDto.builder()
                                .id(2)
                                .name("Petr")
                                .birthday(LocalDate.of(1995, 10, 19))
                                .email("petr@gmail.com")
                                .role(Role.USER)
                                .gender(Gender.MALE)
                                .build()),
                Arguments.of(
                        new User(3, "Sveta", LocalDate.of(2001, 12, 23), "sveta@gmail.com", "321", Role.USER, Gender.FEMALE),
                        UserDto.builder()
                                .id(3)
                                .name("Sveta")
                                .birthday(LocalDate.of(2001, 12, 23))
                                .email("sveta@gmail.com")
                                .role(Role.USER)
                                .gender(Gender.FEMALE)
                                .build()),
                Arguments.of(new User(4, "Vlad", LocalDate.of(1984, 3, 14), "vlad@gmail.com", "456", Role.USER, Gender.MALE),
                        UserDto.builder()
                                .id(4)
                                .name("Vlad")
                                .birthday(LocalDate.of(1984, 3, 14))
                                .email("vlad@gmail.com")
                                .role(Role.USER)
                                .gender(Gender.MALE)
                                .build()),
                Arguments.of(
                        new User(null, "Kate", LocalDate.of(1989, 8, 9), "kate@gmail.com", "777", Role.ADMIN, Gender.FEMALE),
                        UserDto.builder()
                                .name("Kate")
                                .birthday(LocalDate.of(1989, 8, 9))
                                .email("kate@gmail.com")
                                .role(Role.ADMIN)
                                .gender(Gender.FEMALE)
                                .build())
        );
    }
}
