package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.enteties.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    public List<AccountOperation> findByBankAccountId(String accountId);

    public Page<AccountOperation> findByBankAccountId(String accountId, Pageable pageable);

}
