package org.sid.ebankingbackend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.enteties.*;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exception.BalanceNotSufficientException;
import org.sid.ebankingbackend.exception.BankAccountNotFoundException;
import org.sid.ebankingbackend.exception.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor// pour injection de dependance par constructeur il est mieux que d'utiliser Autowired
@Slf4j//log4j pour faire la journalsation
public class BankAccountServiceImpl implements  BanAccountService{
    //Lombok s'occupe de creer cette attribut en utilisant @Slf4j
    //manuellement === Logger logger= LoggerFactory.getLogger(this.getClass().getName());
    //logger.info()
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        //il faut verifier des regles metiers avant d'ajouter qlq chose a la base de données (black list ...)
        log.info("Saving new customer");
        Customer customer =dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }

        CurrentAccount bankAccount= new CurrentAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setOverDraft(overDraft);
        bankAccount.setCustomer(customer);
        CurrentAccount savedBankAccount= bankAccountRepository.save(bankAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interetRate, Long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer==null){
            throw new CustomerNotFoundException("Customer not found");
        }

        SavingAccount bankAccount= new SavingAccount();
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(initialBalance);
        bankAccount.setInterestRate(interetRate);
        bankAccount.setCustomer(customer);
        SavingAccount savedBankAccount= bankAccountRepository.save(bankAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);

    }


    @Override
    public List<CustomerDTO> listCustomers() {

        List<Customer> customers = customerRepository.findAll();
        //programmation fonctionnelle = utilise les stream
        // programmation imperative
        return customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }

    @Override
    public void debit(DebitRequestDTO debitRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount= bankAccountRepository.findById(debitRequestDTO.getIdAccount())
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount.getBalance()<debitRequestDTO.getAmount()){
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(debitRequestDTO.getAmount());
        accountOperation.setDescription(debitRequestDTO.getDescription());
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-debitRequestDTO.getAmount());
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(CreditRequestDTO creditRequestDTO) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(creditRequestDTO.getIdAccount())
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(creditRequestDTO.getAmount());
        accountOperation.setDescription(creditRequestDTO.getDescription());
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+creditRequestDTO.getAmount());
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        DebitRequestDTO debitRequestDTO=new DebitRequestDTO();
        debitRequestDTO.setIdAccount(transferRequestDTO.getIdAccountSource());
        debitRequestDTO.setAmount(transferRequestDTO.getAmount());
        debitRequestDTO.setDescription("transfer to "+transferRequestDTO.getIdAccountDestination());
        debit(debitRequestDTO);
        CreditRequestDTO creditRequestDTO = new CreditRequestDTO();
        creditRequestDTO.setIdAccount(transferRequestDTO.getIdAccountDestination());
        creditRequestDTO.setAmount(transferRequestDTO.getAmount());
        creditRequestDTO.setDescription("transfer from "+ transferRequestDTO.getIdAccountSource());
        credit(creditRequestDTO);
    }
    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts= bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS=bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount= (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer =customerRepository.findById(customerId)
        .orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
        return  dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        //il faut verifier des regles metiers avant d'ajouter qlq chose a la base de données (black list ...)
        log.info("Updating new customer");
        Customer customer =dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> accountOperationDTOS=accountOperations.stream().map(accountOperation ->
             dtoMapper.fromAccountOperation(accountOperation)
        ).collect(Collectors.toList());
        return accountOperationDTOS;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFoundException("Account not found");
        Page<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS =accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

}
