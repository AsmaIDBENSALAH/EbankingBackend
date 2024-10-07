package org.sid.ebankingbackend.web;

import lombok.AllArgsConstructor;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.exception.BalanceNotSufficientException;
import org.sid.ebankingbackend.exception.BankAccountNotFoundException;
import org.sid.ebankingbackend.services.BanAccountService;
import org.sid.ebankingbackend.services.BankAccountServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestAPI {
    private BanAccountService bankAccountService;
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();

    }
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page", defaultValue="0") int page,
            @RequestParam(name="size", defaultValue="5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping("/accounts/debit")
    public void debit(@RequestBody DebitRequestDTO debitRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(debitRequestDTO);
    }

    @PostMapping("/accounts/credit")
    public void credit(@RequestBody CreditRequestDTO creditRequestDTO) throws BankAccountNotFoundException{
        bankAccountService.credit(creditRequestDTO);
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(transferRequestDTO);
    }

}
