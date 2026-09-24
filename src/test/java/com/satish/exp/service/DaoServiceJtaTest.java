package com.satish.exp.service;

import com.satish.exp.repo.UserRepository;
import com.satish.exp.repo.UserRepository2;
import com.satish.exp.repo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DaoServiceJtaTest {

    @Autowired
    private DaoService daoService;

    @Autowired
    private UserRepository userRepository; // Primary database repository

    @Autowired
    private UserRepository2 userRepository2; // Secondary database repository

    @Autowired
    @Qualifier("jdbcTemplate2")
    private NamedParameterJdbcTemplate jdbcTemplate2;

    @BeforeEach
    public void setup() {
        // Clean up both databases before each test to ensure a clean state
        userRepository.deleteAll();
        try {
            jdbcTemplate2.getJdbcOperations().execute("TRUNCATE TABLE USER_INFO");
        } catch (Exception e) {
            // If the table doesn't exist yet, we can let it be created or ignore this error
        }
    }

    @Test
    public void testJtaCommitSuccess() {
        User user1 = new User(null, "Alice Primary", "alice@primary.com");
        User user2 = new User(102, "Bob Secondary", "bob@secondary.com");

        // Run distributed transaction
        daoService.addUsersDistributed(user1, user2);

        // Verify primary database has user1 (by generated id)
        assertNotNull(user1.getId());
        Optional<User> savedUser1 = userRepository.findById(user1.getId());
        assertTrue(savedUser1.isPresent());
        assertEquals("Alice Primary", savedUser1.get().getName());

        // Verify secondary database has user2 (by explicit id 102)
        Optional<User> savedUser2 = userRepository2.findById(102L);
        assertTrue(savedUser2.isPresent());
        assertEquals("Bob Secondary", savedUser2.get().getName());
    }

    @Test
    public void testJtaRollbackOnFailure() {
        User user1 = new User(null, "Charlie Primary", "charlie@primary.com");
        User user2 = new User(202, "David Secondary", "david@secondary.com");

        // Run transaction that is guaranteed to throw an exception
        assertThrows(RuntimeException.class, () -> {
            daoService.addUsersDistributedWithFailure(user1, user2);
        });

        // Verify rollback in primary database: count should be 0
        assertEquals(0, userRepository.count());

        // Verify rollback in secondary database: user2 should NOT be saved
        Optional<User> savedUser2 = userRepository2.findById(202L);
        assertFalse(savedUser2.isPresent());
    }
}
