package org.example.enbankspring.services;

import org.example.enbankspring.dtos.CustomerDTO;
import org.example.enbankspring.entities.BankAccount;
import org.example.enbankspring.entities.CurrentAccount;
import org.example.enbankspring.entities.Customer;
import org.example.enbankspring.entities.SavingAccount;
import org.example.enbankspring.exceptions.AccountBalanceInsiffisantException;
import org.example.enbankspring.exceptions.BankAccountNotFoudException;
import org.example.enbankspring.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    Customer createCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDravt , Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interest , Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String string) throws BankAccountNotFoudException;

    void debit(String acountId , String description, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException;
    void credit(String acountId , String description, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException;
    void transfer(String accountSource , String accountDest, double amount) throws BankAccountNotFoudException, AccountBalanceInsiffisantException;


    List<BankAccount> bankAccountList();
}
