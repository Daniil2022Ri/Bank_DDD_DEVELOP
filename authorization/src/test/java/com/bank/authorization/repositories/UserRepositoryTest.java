package com.bank.authorization.repositories;


import com.bank.authorization.entities.Role;
import com.bank.authorization.entities.User;
import com.bank.authorization.utils.UserFabricForTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static com.bank.authorization.utils.TestConstant.DATABASE_NAME;
import static com.bank.authorization.utils.TestConstant.DATABASE_PASSWORD;
import static com.bank.authorization.utils.TestConstant.DATABASE_USERNAME;
import static com.bank.authorization.utils.TestConstant.FIRST_USER_PROFILE_ID;
import static com.bank.authorization.utils.TestConstant.NEW_PASSWORD;
import static com.bank.authorization.utils.TestConstant.NOT_EXISTS_PROFILE;
import static com.bank.authorization.utils.TestConstant.SECOND_SEARCH_ID;
import static com.bank.authorization.utils.TestConstant.SECOND_USER_PROFILE_ID;
import static com.bank.authorization.utils.TestConstant.UPDATED_PROFILE_ID;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName(DATABASE_NAME)
            .withPassword(DATABASE_PASSWORD)
            .withUsername(DATABASE_USERNAME);

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.enabled", () -> false);
    }

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = UserFabricForTest.buildUserForTestRepository(FIRST_USER_PROFILE_ID);
        user2 = UserFabricForTest.buildUserForTestRepository(SECOND_USER_PROFILE_ID);
    }

    @Test
    void findAll_empty() {
        List<User> users = userRepository.findAll();
        Assertions.assertThat(users).isEmpty();
    }

    @Test
    void save() {
        User actualUser = userRepository.save(user1);

        Assertions.assertThat(actualUser).isNotNull();
        Assertions.assertThat(actualUser.getId()).isPositive();
        Assertions.assertThat(actualUser.getProfileId()).isEqualTo(FIRST_USER_PROFILE_ID);
    }

    @Test
    void update() {
        User existUser = userRepository.save(user1);
        existUser.setPassword(NEW_PASSWORD);
        existUser.setProfileId(UPDATED_PROFILE_ID);
        existUser.setRole(Role.ROLE_USER);

        User actualUser = userRepository.save(existUser);

        Assertions.assertThat(actualUser).isEqualTo(existUser);
        Assertions.assertThat(actualUser.getPassword()).isEqualTo(NEW_PASSWORD);
    }

    @Test
    void findAll() {
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        Assertions.assertThat(users).hasSize(2);
        Assertions.assertThat(users).extracting(User::getProfileId)
                .containsExactlyInAnyOrder(user1.getProfileId(), user2.getProfileId());
        Assertions.assertThat(users).extracting(User::getId).isNotEmpty();
    }

    @Test
    void findById_notExist() {
        userRepository.save(user1);

        Optional<User> user = userRepository.findById(SECOND_SEARCH_ID);

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    void findById() {
        User savedUser = userRepository.save(user1);

        Optional<User> user = userRepository.findById(savedUser.getId());

        Assertions.assertThat(user).isPresent();
        Assertions.assertThat(user.get().getId()).isPositive();
        Assertions.assertThat(user.get().getProfileId()).isEqualTo(user1.getProfileId());
        Assertions.assertThat(user.get().getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(user.get().getRole()).isEqualTo(user1.getRole());
    }

    @Test
    void findByProfileId() {
        userRepository.save(user2);

        Optional<User> user = userRepository.findByProfileId(SECOND_USER_PROFILE_ID);

        Assertions.assertThat(user).isPresent();
        Assertions.assertThat(user.get().getId()).isPositive();
        Assertions.assertThat(user.get().getProfileId()).isEqualTo(user2.getProfileId());
        Assertions.assertThat(user.get().getPassword()).isEqualTo(user2.getPassword());
        Assertions.assertThat(user.get().getRole()).isEqualTo(user2.getRole());
    }

    @Test
    void findByProfileId_notExist() {
        userRepository.save(user2);

        Optional<User> user = userRepository.findByProfileId(NOT_EXISTS_PROFILE);

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    void delete() {
        User userToDelete = userRepository.save(user1);
        userRepository.save(user2);

        List<User> afterUsers = userRepository.findAll();
        Assertions.assertThat(afterUsers).hasSize(2);

        userRepository.delete(userToDelete);

        List<User> beforeUsers = userRepository.findAll();
        Assertions.assertThat(beforeUsers).hasSize(1);
        Assertions.assertThat(beforeUsers).isNotEqualTo(afterUsers);
        Assertions.assertThat(beforeUsers).extracting(User::getProfileId)
                .containsExactlyInAnyOrder(user2.getProfileId());

    }

    @Test
    void delete_notExistUser() {
        userRepository.save(user1);

        List<User> afterUsers = userRepository.findAll();
        Assertions.assertThat(afterUsers).hasSize(1);

        userRepository.delete(user2);

        List<User> beforeUsers = userRepository.findAll();
        Assertions.assertThat(beforeUsers).hasSize(1);
        Assertions.assertThat(beforeUsers).isEqualTo(afterUsers);
        Assertions.assertThat(beforeUsers).extracting(User::getProfileId)
                .containsExactlyInAnyOrder(user1.getProfileId());

    }

}