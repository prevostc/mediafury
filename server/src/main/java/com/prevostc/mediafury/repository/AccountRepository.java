package com.prevostc.mediafury.repository;

import com.prevostc.mediafury.model.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Account findByUsername(String username);
}

