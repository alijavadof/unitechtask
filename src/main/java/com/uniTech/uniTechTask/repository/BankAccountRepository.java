package com.uniTech.uniTechTask.repository;

import com.uniTech.uniTechTask.model.BankAccount;
import com.uniTech.uniTechTask.dto.ActiveAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    @Query(value = "SELECT ba.account_number accountNumber , ba.balance , ba.created_date createdDate " +
            "FROM bank_account ba JOIN users u " +
            "ON ba.user_id = u.id WHERE u.pin = ?1 AND ba.is_active", nativeQuery = true)
    List<ActiveAccount> findAllAccountByPin(String pin);

    Optional<BankAccount> findBankAccountByAccountNumber(String toAccountNumber);

    Optional<BankAccount> findBankAccountByUserIdAndAccountNumber(long id, String accountNumber);
}
