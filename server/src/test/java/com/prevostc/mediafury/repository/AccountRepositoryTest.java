package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.model.Account;
import com.prevostc.mediafury.model.Movie;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @After
    public void tearDown() throws Exception {
        accountRepository.deleteAll();
    }

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findByUsername_worksAsExpected() {
        // Arrange
        Account user = new Account("John", "pass");
        accountRepository.save(user);

        // Act
        Account res = accountRepository.findByUsername("John");

        // Assert
        assertThat(res).isEqualTo(user);
    }
}
