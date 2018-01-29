package com.prevostc.mediafury.commandline;

import com.prevostc.mediafury.model.Account;
import com.prevostc.mediafury.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountCommandLineRunner implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public AccountCommandLineRunner(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) {
        accountRepository.save(new Account("admin", "admin"));
    }
}