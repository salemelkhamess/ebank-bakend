package org.example.enbankspring.services;

import jakarta.transaction.Transactional;
import org.example.enbankspring.dtos.CustomerDTO;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional

public class BankAccountServiceImpl implements BankAccountService {
    CustomerRepository customerRepository;
    BankAccontRepository bankAccountRepository;
    AccountOperationRepository accountOperationRepository;
    BankAccountMapperImpl bankAccountMapper;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccontRepository bankAccountRepository, AccountOperationRepository accountOperationRepository , BankAccountMapperImpl mapper) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.bankAccountMapper = mapper;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        log.info("createCustomer");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
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


        return currentAccountSaved;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interest, Long customerId) throws CustomerNotFoundException {
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

        return savingAccount1;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customer =  customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customer.stream().map(c -> bankAccountMapper.fromCustomer(c)).collect(Collectors.toList());
        return customerDTOList;
    }

    @Override
    public BankAccount getBankAccount(String id) throws BankAccountNotFoudException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new BankAccountNotFoudException("Account not found"));
        return bankAccount;
    }

    @Override
    public void debit(String acountId, String description, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException {

        BankAccount bankAccount = getBankAccount(acountId);

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
        BankAccount bankAccount = getBankAccount(acountId);

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
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }
}
