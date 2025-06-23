package org.example.enbankspring.services;

import org.example.enbankspring.dtos.*;
import org.example.enbankspring.entities.*;
import org.example.enbankspring.exceptions.AccountBalanceInsiffisantException;
import org.example.enbankspring.exceptions.BankAccountNotFoudException;
import org.example.enbankspring.exceptions.CustomerNotFoundException;
import java.util.List;

public interface BankAccountService {

    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDravt , Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interest , Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String string) throws BankAccountNotFoudException;

    void debit(String acountId , String description, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException;
    void credit(String acountId , String description, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException;
    void transfer(String accountSource , String accountDest, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException;

    List<BankAccountDTO> bankAccountList();
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deletCustomer(Long id);

    List<AccountOperationDTO> accountHistory(String id);

    AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoudException;

    List<CustomerDTO> searchCustomer(String search);
}