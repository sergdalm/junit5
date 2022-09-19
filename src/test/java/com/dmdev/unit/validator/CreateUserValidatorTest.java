package com.dmdev.unit.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({
        MockitoExtension.class
})
public class CreateUserValidatorTest {

    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();

    @ParameterizedTest
    @MethodSource("getValidDto")
    void shouldReturnEmptyValidationResultIfDtoIsValid(CreateUserDto createUserDto) {
        ValidationResult validationResult = createUserValidator.validate(createUserDto);

        assertThat(validationResult.getErrors()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("getInvalidDto")
    void shouldReturnValidationResultWithErrorsIfDtoIsInvalid(CreateUserDto createUserDto, int numberOfErrors) {
        ValidationResult validationResult = createUserValidator.validate(createUserDto);

        assertThat(validationResult.getErrors()).isNotEmpty();
        assertEquals(validationResult.getErrors().size(), numberOfErrors);
    }

    static Stream<Arguments> getValidDto() {
        return Stream.of(
                Arguments.of(CreateUserDto.builder()
                        .email("ivan@gmail.com")
                        .name("Ivan")
                        .birthday("1993-07-12")
                        .password("1111")
                        .gender("MALE")
                        .role("USER")
                        .build()),
                Arguments.of(CreateUserDto.builder()
                        .email("petr@gmail.com")
                        .name("Petr")
                        .birthday("1990-06-04")
                        .password("34995")
                        .gender("MALE")
                        .role("ADMIN")
                        .build()),
                Arguments.of(CreateUserDto.builder()
                        .email("sveta@gmail.com")
                        .name("Sveta")
                        .birthday("1996-01-20")
                        .password("4r4r4")
                        .gender("FEMALE")
                        .role("USER")
                        .build())
        );
    }

    static Stream<Arguments> getInvalidDto() {
        return Stream.of(
                Arguments.of(CreateUserDto.builder()
                        .email("ivan@gmail.com")
                        .name("Ivan")
                        .birthday("04944")
                        .password("1111")
                        .gender("MALE")
                        .role("USER")
                        .build(), 1),
                Arguments.of(CreateUserDto.builder()
                        .email("petr@gmail.com")
                        .name("Petr")
                        .birthday("1990-06-04")
                        .password("34995")
                        .gender("ANOTHER")
                        .role("ADMIN")
                        .build(), 1),
                Arguments.of(CreateUserDto.builder()
                        .email("sveta@gmail.com")
                        .name("Sveta")
                        .birthday("1996-01-20")
                        .password("4r4r4")
                        .gender("FEMALE")
                        .role("GAMER")
                        .build(), 1),
                Arguments.of(CreateUserDto.builder()
                        .email("sveta@gmail.com")
                        .name("Sveta")
                        .birthday("1996-01-20")
                        .password("4r4r4")
                        .gender("TRANSGENDER")
                        .role("GAMER")
                        .build(), 2),
                Arguments.of(CreateUserDto.builder()
                        .email("sveta@gmail.com")
                        .name("Sveta")
                        .birthday("8449")
                        .password("4r4r4")
                        .gender("TRANSGENDER")
                        .role("GAMER")
                        .build(), 3)
        );
    }
}
