package org.example.enbankspring;

import org.example.enbankspring.entities.AccountOperation;
import org.example.enbankspring.entities.CurrentAccount;
import org.example.enbankspring.entities.Customer;
import org.example.enbankspring.entities.SavingAccount;
import org.example.enbankspring.enums.AccountEnum;
import org.example.enbankspring.enums.OperationType;
import org.example.enbankspring.repositories.AccountOperationRepository;
import org.example.enbankspring.repositories.BankAccontRepository;
import org.example.enbankspring.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EnbankSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnbankSpringApplication.class, args);
    }

    @Bean
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
                    operationRepository.save(accountOperation);
                }
            });


        };
    }

}
