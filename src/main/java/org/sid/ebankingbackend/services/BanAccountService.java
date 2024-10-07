package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.enteties.BankAccount;
import org.sid.ebankingbackend.enteties.CurrentAccount;
import org.sid.ebankingbackend.enteties.SavingAccount;
import org.sid.ebankingbackend.exception.BalanceNotSufficientException;
import org.sid.ebankingbackend.exception.BankAccountNotFoundException;
import org.sid.ebankingbackend.exception.CustomerNotFoundException;

import java.util.List;

public interface BanAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double  initialBalance, double overDraft , Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double  initialBalance, double interetRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;


    void debit(DebitRequestDTO debitRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(CreditRequestDTO creditRequestDTO) throws BankAccountNotFoundException;


    void transfer(TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
