package org.sid.ebankingbackend;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.enteties.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exception.BalanceNotSufficientException;
import org.sid.ebankingbackend.exception.BankAccountNotFoundException;
import org.sid.ebankingbackend.exception.CustomerNotFoundException;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.BanAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BanAccountService banAccountService){
        return args -> {
            Stream.of("Hassan","Imane","Mohamed").forEach(name ->
            {
                CustomerDTO customer= new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                banAccountService.saveCustomer(customer);
            });
            banAccountService.listCustomers().forEach(customer -> {
                try {
                    banAccountService.saveCurrentBankAccount(Math.random()*90000,9000, customer.getId());
                    banAccountService.saveSavingBankAccount(Math.random()*90000,5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccountList=banAccountService.bankAccountList();
            for(BankAccountDTO bankAccount : bankAccountList){
                for(int i=0 ; i<10; i++){
                    String accountID;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accountID= ((SavingBankAccountDTO) bankAccount).getId();
                    }else{
                        accountID= ((CurrentBankAccountDTO) bankAccount).getId();

                    }
                    DebitRequestDTO debitRequestDTO=new DebitRequestDTO();
                    debitRequestDTO.setIdAccount(accountID);
                    debitRequestDTO.setAmount(10000+Math.random()*9000);
                    debitRequestDTO.setDescription("debit");
                    CreditRequestDTO creditRequestDTO= new CreditRequestDTO();
                    creditRequestDTO.setIdAccount(accountID);
                    creditRequestDTO.setAmount(10000+Math.random()*120000);
                    creditRequestDTO.setDescription("credi");
                    banAccountService.credit(creditRequestDTO);
                    banAccountService.debit(debitRequestDTO);
                }
            }
        };
    }
    //@Bean
    CommandLineRunner commandLineRunner(
                            BankAccountRepository bankAccountRepository){
        return args -> {
            BankAccount bankAccount= bankAccountRepository.findById("14cd702b-d07a-4929-a643-c1cb85a6e2c2").orElse(null);
            if(bankAccount != null) {
                System.out.println("***************************");
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getAccountStatus());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                System.out.println(bankAccount.getClass().getSimpleName());
                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("over draft" + ((CurrentAccount) bankAccount).getOverDraft());
                } else if (bankAccount instanceof SavingAccount) {
                    System.out.println("Rate Interest" + ((SavingAccount) bankAccount).getInterestRate());
                }
                bankAccount.getAccountOperations().forEach(op -> {
                    System.out.println("-----------------------");
                    System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());

                });
            }
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name ->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer ->
            {
                CurrentAccount currentAccount=new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setAccountStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount =new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setAccountStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll().forEach(acc -> {
                for (int i=0 ; i<10 ; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });

        };
    }
}
/*

* */