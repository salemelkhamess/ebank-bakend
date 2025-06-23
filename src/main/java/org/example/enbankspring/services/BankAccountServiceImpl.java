package org.example.enbankspring.services;
import jakarta.transaction.Transactional;
import org.example.enbankspring.dtos.*;
import org.example.enbankspring.entities.*;
import org.example.enbankspring.enums.OperationType;
import org.example.enbankspring.exceptions.AccountBalanceInsiffisantException;
import org.example.enbankspring.exceptions.BankAccountNotFoudException;
import org.example.enbankspring.exceptions.CustomerNotFoundException;
import org.example.enbankspring.mappers.BankAccountMapperImpl;
import org.example.enbankspring.repositories.AccountOperationRepository;
import org.example.enbankspring.repositories.BankAccontRepository;
import org.example.enbankspring.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.service.OpenAPIService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional

public class BankAccountServiceImpl implements BankAccountService {
    private final OpenAPIService openAPIBuilder;
    CustomerRepository customerRepository;
    BankAccontRepository bankAccountRepository;
    AccountOperationRepository accountOperationRepository;
    BankAccountMapperImpl bankAccountMapper;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccontRepository bankAccountRepository, AccountOperationRepository accountOperationRepository , BankAccountMapperImpl mapper, OpenAPIService openAPIBuilder) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.bankAccountMapper = mapper;
        this.openAPIBuilder = openAPIBuilder;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("createCustomer");
        Customer customer = bankAccountMapper.toCustomer(customerDTO);


        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            log.error("Customer not found");
            throw new CustomerNotFoundException("Customer not found");
        }
        log.info("saveBankAccount");
        CurrentAccount  currentAccount = new  CurrentAccount() ;


        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount currentAccountSaved = bankAccountRepository.save(currentAccount);


        return bankAccountMapper.fromCurrentAccount(currentAccountSaved);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interest, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            log.error("Customer not found");
            throw new CustomerNotFoundException("Customer not found");
        }
        log.info("saveBankAccount");
        SavingAccount savingAccount = new SavingAccount() ;


        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interest);
        SavingAccount savingAccount1 = bankAccountRepository.save(savingAccount);

        return bankAccountMapper.fromSavingBankAccount(savingAccount1);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customer =  customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customer.stream().map(c -> bankAccountMapper.fromCustomer(c)).collect(Collectors.toList());
        return customerDTOList;
    }

    @Override
    public BankAccountDTO getBankAccount(String id) throws BankAccountNotFoudException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new BankAccountNotFoudException("Account not found"));
        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String acountId, String description, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException {

        BankAccount bankAccount = bankAccountRepository.findById(acountId).orElseThrow(() -> new BankAccountNotFoudException("Account not found"));
        if (bankAccount.getBalance() < amount) {
            throw new AccountBalanceInsiffisantException("Balance insiffisant ");

        }

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String acountId, String description, double amount) throws BankAccountNotFoudException {
        BankAccount bankAccount = bankAccountRepository.findById(acountId).orElseThrow(() -> new BankAccountNotFoudException("Account not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountSource, String accountDest, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException {

        debit(accountSource , "Transfer to"+accountDest , amount);
        credit(accountDest , "Transfer from"+accountSource , amount);

    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankaccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTO = bankaccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return  bankAccountDTO;
    }


    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("Customer not fount"));
        return bankAccountMapper.fromCustomer(customer);
    }



    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("createCustomer");
        Customer customer = bankAccountMapper.toCustomer(customerDTO);


        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }


    @Override
    public void deletCustomer(Long id)  {
        log.info("deleteCustomer");
        customerRepository.deleteById(id);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String id){
        List<AccountOperation> byBankAccountId = accountOperationRepository.findByBankAccountId(id);

       return byBankAccountId.stream().map(op->{
            return bankAccountMapper.fromAccountOperation(op);
        }).collect(Collectors.toList());
        

    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoudException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoudException("Id not fount");
        }

        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(id, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOList = accountOperations.getContent().stream().map(op -> bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());

        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOList);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setSize(size);
        accountHistoryDTO.setCurrentPages(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());


        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomer(String search) {
        List<Customer> customerList = customerRepository.searchCustomer(search);
        List<CustomerDTO> customerDto = customerList.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());

        return customerDto;
    }


}
