package org.example.enbankspring;

import org.example.enbankspring.entities.*;
import org.example.enbankspring.enums.AccountEnum;
import org.example.enbankspring.enums.OperationType;
import org.example.enbankspring.exceptions.AccountBalanceInsiffisantException;
import org.example.enbankspring.exceptions.BankAccountNotFoudException;
import org.example.enbankspring.exceptions.CustomerNotFoundException;
import org.example.enbankspring.repositories.AccountOperationRepository;
import org.example.enbankspring.repositories.BankAccontRepository;
import org.example.enbankspring.repositories.CustomerRepository;
import org.example.enbankspring.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EnbankSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnbankSpringApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Salem" , "Hassen" , "Minetou").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                bankAccountService.createCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5,customer.getId());
                    List<BankAccount> bankAccountList = bankAccountService.bankAccountList();
                    for (BankAccount bankAccount : bankAccountList) {
                        bankAccountService.credit(bankAccount.getId() ,"Credit" ,10000 + Math.random()*12000);
                        bankAccountService.debit(bankAccount.getId() ,"Debit" ,1000 + Math.random()*9000);

                    }
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                } catch (BankAccountNotFoudException e) {
                    throw new RuntimeException(e);
                } catch (AccountBalanceInsiffisantException e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository , BankAccontRepository accontRepository , AccountOperationRepository operationRepository) {
        return args -> {

            Stream.of("Salem" , "Ahmed" , "Mohamed").forEach(name-> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setCustomer(customer);
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setStatus(AccountEnum.CREATED);
                currentAccount.setOverDraft(9000);
                currentAccount.setCreatedAt(new Date());
                accontRepository.save(currentAccount);
            });


            customerRepository.findAll().forEach(customer -> {
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());

                savingAccount.setCustomer(customer);
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setStatus(AccountEnum.CREATED);
                savingAccount.setInterestRate(5.5);
                savingAccount.setCreatedAt(new Date());
                accontRepository.save(savingAccount);
            });


            accontRepository.findAll().forEach(acc->{
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setBankAccount(acc);
                    accountOperation.setOperationDate(new Date());
                accountOperation.setAmount(Math.random()*9000);
                accountOperation.setOperationType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
                accountOperation.setDescription("DES"+i);
                    operationRepository.save(accountOperation);
                }
            });


        };
    }

}
