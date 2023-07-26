package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.UserCleanupService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@DisplayName("test for the UserCleanupService")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class UserCleanupServiceTest {
    @Autowired
    private UserCleanupService userCleanupService;

    @Autowired
    private UserRepository userRepository;

    @Value("${time.register-expiration-time}")
    private int expirationTime;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("clean user who is out the expiration time")
    void cleanup_givenUserOutExpirationTime_returnsNull() {
        User user = new User("Sergio", "sergio@example.com", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));
        userRepository.save(user);

        userCleanupService.cleanupUser();

        assertThat(userRepository.getUserByEmail(user.getEmail())).isNull();

    }

    @Test
    @DisplayName("clean users who are out the expiration time")
    void cleanup_givenUsersOutExpirationTime_returnsEmpty() {
        User user1 = new User("user1@example.com", "User 1", "password");
        user1.setCreatedAt(user1.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));

        User user2 = new User("user2@example.com", "User 2", "password");
        user2.setCreatedAt(user2.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));

        User user3 = new User("user3@example.com", "user3", "password");
        user3.setCreatedAt(user3.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));

        userRepository.saveAll(List.of(user1, user2, user3));

        userCleanupService.cleanupUser();

        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("do not clean user who is within the expiration time")
    void cleanup_givenUserWithinExpirationTime_returnsValidUser() {
        User user = new User("Sergio", "sergio@example.com", "password");
        userRepository.save(user);

        userCleanupService.cleanupUser();

        User receivedUser = userRepository.getUserByEmail(user.getEmail());

        assertThat(userRepository.getUserByEmail(user.getEmail())).isNotNull();
        assertEquals(receivedUser.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("do not clean users who are within the expiration time")
    void cleanup_givenUsersWithinExpirationTime_returnsValidUsers() {
        User user1 = new User("user1@example.com", "User 1", "password");
        User user2 = new User("user2@example.com", "User 2", "password");
        User user3 = new User("user3@example.com", "user3", "password");

        userRepository.saveAll(List.of(user1, user2, user3));

        userCleanupService.cleanupUser();

        User receivedUser1 = userRepository.getUserByEmail("user1@example.com");
        User receivedUser2 = userRepository.getUserByEmail("user2@example.com");
        User receivedUser3 = userRepository.getUserByEmail("user3@example.com");

        assertEquals(receivedUser1.getEmail(), user1.getEmail());
        assertEquals(receivedUser2.getEmail(), user2.getEmail());
        assertEquals(receivedUser3.getEmail(), user3.getEmail());
    }

}
