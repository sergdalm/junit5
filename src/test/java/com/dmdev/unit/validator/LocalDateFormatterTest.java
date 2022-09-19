package com.dmdev.unit.validator;

import com.dmdev.util.LocalDateFormatter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalDateFormatterTest {

    @ParameterizedTest
    @MethodSource("getValidDataAsString")
    void shouldReturnTrueIfDataIsValid(String data) {
        boolean result = LocalDateFormatter.isValid(data);
        assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("getInvalidDataAsString")
    void shouldReturnFalseIfDataIsInvalid(String data) {
        boolean result = LocalDateFormatter.isValid(data);
        assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("getDataAsStringAndLocalData")
    void shouldReturnDataAsLocalData(String data, LocalDate localDate) {
        LocalDate resultDate = LocalDateFormatter.format(data);
        assertEquals(localDate, resultDate);
    }

    static Stream<Arguments> getValidDataAsString() {
        return Stream.of(
                Arguments.of("1990-01-31"),
                Arguments.of("2000-02-20"),
                Arguments.of("1980-03-09"),
                Arguments.of("1983-04-05"),
                Arguments.of("1965-05-18"),
                Arguments.of("1964-06-29"),
                Arguments.of("2015-07-22"),
                Arguments.of("1959-08-01"),
                Arguments.of("1967-09-03"),
                Arguments.of("1992-10-28"),
                Arguments.of("2010-11-19"),
                Arguments.of("2011-12-24")
        );
    }

    static Stream<Arguments> getInvalidDataAsString() {
        return Stream.of(
                Arguments.of("30000-01-31"),
                Arguments.of("2000-02--01"),
                Arguments.of("1980-90-09"),
                Arguments.of("19OO-04-05"),
                Arguments.of("1965-05-38"),
                Arguments.of("1964-13-29"),
                Arguments.of("20-0-07-22"),
                Arguments.of("19+9-08-01"),
                Arguments.of("1&67-09-03"),
                Arguments.of("1992-1O-28"),
                Arguments.of("2010-00-19"),
                Arguments.of("2011-12-00"),
                Arguments.of("0000-12-00")
        );
    }

    static Stream<Arguments> getDataAsStringAndLocalData() {
        return Stream.of(
                Arguments.of("1990-01-01", LocalDate.of(1990, 1, 1)),
                Arguments.of("2000-02-20", LocalDate.of(2000, 2, 20)),
                Arguments.of("1980-03-09", LocalDate.of(1980, 3, 9)),
                Arguments.of("1983-04-05", LocalDate.of(1983, 4, 5)),
                Arguments.of("1965-05-18", LocalDate.of(1965, 5, 18)),
                Arguments.of("1964-06-29", LocalDate.of(1964, 6, 29)),
                Arguments.of("2015-07-22", LocalDate.of(2015, 7, 22)),
                Arguments.of("1959-08-01", LocalDate.of(1959, 8, 1)),
                Arguments.of("1967-09-03", LocalDate.of(1967, 9, 3)),
                Arguments.of("1992-10-28", LocalDate.of(1992, 10, 28)),
                Arguments.of("2010-11-19", LocalDate.of(2010, 11, 19)),
                Arguments.of("2011-12-24", LocalDate.of(2011, 12, 24))
        );
    }
}
