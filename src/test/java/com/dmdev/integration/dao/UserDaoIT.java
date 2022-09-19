package com.dmdev.integration.dao;

import com.dmdev.dao.UserDao;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserDaoIT extends IntegrationTestBase {
    private final static User IVAN = new User(1, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", Role.ADMIN, Gender.MALE);
    private final static User PETR = new User(2, "Petr", LocalDate.of(1995, 10, 19), "petr@gmail.com", "123", Role.USER, Gender.MALE);
    private final static User SVETA = new User(3, "Sveta", LocalDate.of(2001, 12, 23), "sveta@gmail.com", "321", Role.USER, Gender.FEMALE);
    private final static User VLAD = new User(4, "Vlad", LocalDate.of(1984, 3, 14), "vlad@gmail.com", "456", Role.USER, Gender.MALE);
    private final static User KATE = new User(5, "Kate", LocalDate.of(1989, 8, 9), "kate@gmail.com", "777", Role.ADMIN, Gender.FEMALE);
    private final static User USER = new User(null, "Kolya", LocalDate.of(1993, 7, 12), "kolya@gmail.com", "1111", Role.USER, Gender.MALE);

    private final UserDao userDao = UserDao.getInstance();

    @Test
    void shouldInitializeUserDao() {
        UserDao instance = UserDao.getInstance();
        assertThat(instance).isNotNull();
    }

    @Test
    void shouldReturnNotEmptyListIfUsersExistInDataBase() {
        List<User> all = userDao.findAll();
        assertThat(all).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("getIdAndUsersForTest")
    void shouldReturnExistingUser(int id, User user) {
        Optional<User> optionalUser = userDao.findById(id);

        assertThat(optionalUser).isPresent();
        assertEquals(optionalUser.get(), user);
    }

    static Stream<Arguments> getIdAndUsersForTest() {
        return Stream.of(
                Arguments.of(1, IVAN),
                Arguments.of(2, PETR),
                Arguments.of(3, SVETA),
                Arguments.of(4, VLAD),
                Arguments.of(5, KATE)
        );
    }

    @ParameterizedTest
    @MethodSource("getEmailPasswordAndUserForTest")
    void shouldReturnExistingUserByEmailAndPassword(String email, String password, User user) {
        Optional<User> optionalUser = userDao.findByEmailAndPassword(email, password);

        assertThat(optionalUser).isPresent();
        assertEquals(optionalUser.get(), user);
    }

    static Stream<Arguments> getEmailPasswordAndUserForTest() {
        return Stream.of(
                Arguments.of("ivan@gmail.com", "111", IVAN),
                Arguments.of("petr@gmail.com", "123", PETR),
                Arguments.of("sveta@gmail.com", "321", SVETA),
                Arguments.of("vlad@gmail.com", "456", VLAD),
                Arguments.of("kate@gmail.com", "777", KATE)
        );
    }

    // This test includes calling two methods (save() and delete()) to ensure sequential calling
    // of these methods because JUnit 5's test ordering means
    // can't guarantee test order
    @Test
    void shouldSaveAndDeleteNewUser() {
        User savedUser = userDao.save(USER);

        assertEquals(USER.getEmail(), savedUser.getEmail());
        assertEquals(USER.getName(), savedUser.getName());
        assertEquals(USER.getBirthday(), savedUser.getBirthday());
        assertEquals(USER.getPassword(), savedUser.getPassword());
        assertEquals(USER.getRole(), savedUser.getRole());
        assertEquals(USER.getGender(), savedUser.getGender());

        boolean delete = userDao.delete(savedUser.getId());
        assertTrue(delete);
    }

    @ParameterizedTest
    @MethodSource("getValuesForUpdateTest")
    <T> void shouldUpdateUserAndReturnToPreviousValue(User user, BiConsumer<User, T> setValue, Function<User, T> getValue, T currentValue, T newValue) {
        setValue.accept(user, newValue);
        userDao.update(user);

        Optional<User> updatedOptionalUser = userDao.findById(user.getId());

        assertThat(updatedOptionalUser).isPresent();
        assertEquals(user, updatedOptionalUser.get());

        setValue.accept(user, currentValue);
        userDao.update(user);

        Optional<User> optionalUser = userDao.findById(user.getId());

        assertThat(optionalUser).isPresent();
        assertEquals(currentValue, getValue.apply(optionalUser.get()));
    }

    static Stream<Arguments> getValuesForUpdateTest() {
        return Stream.of(
                Arguments.of(IVAN, (BiConsumer<User, String>) User::setEmail, (Function<User, String>) User::getEmail, IVAN.getEmail(), "ivandkdkdkd@gmail.com"),
                Arguments.of(IVAN, (BiConsumer<User, String>) User::setPassword, (Function<User, String>) User::getPassword, IVAN.getPassword(), "458uhn34fh034f"),
                Arguments.of(IVAN, (BiConsumer<User, String>) User::setName, (Function<User, String>) User::getName, IVAN.getName(), "Ivanushka"),
                Arguments.of(IVAN, (BiConsumer<User, Role>) User::setRole, (Function<User, Role>) User::getRole, IVAN.getRole(), Role.USER),
                Arguments.of(IVAN, (BiConsumer<User, Gender>) User::setGender, (Function<User, Gender>) User::getGender, IVAN.getGender(), Gender.FEMALE),
                Arguments.of(IVAN, (BiConsumer<User, LocalDate>) User::setBirthday, (Function<User, LocalDate>) User::getBirthday, IVAN.getBirthday(), LocalDate.of(1990, 1, 1))
        );
    }

    @Test
    void shouldReturnEmptyOptionalIfUserNotExistsById() {
        Optional<User> optionalUser = userDao.findById(Integer.MAX_VALUE);
        assertThat(optionalUser).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptionalIfUserNotExistsByEmailAndPassword() {
        Optional<User> optionalUser = userDao.findByEmailAndPassword("!@#$%^&*()_+", "");
        assertThat(optionalUser).isEmpty();
    }

    @Test
    void shouldReturnFalseWhenDeleteNotExistingUser() {
        boolean result = userDao.delete(-1);
        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenSaveInvalidEntity() {
        assertThrows(Exception.class, () -> userDao.save(new User(null, null, null, null, null, null, null)));
    }
}
