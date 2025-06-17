package org.example.enbankspring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enbankspring.enums.AccountEnum;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "TYPE" , length = 4,discriminatorType = DiscriminatorType.STRING)
public class BankAccount {
    @Id
    public String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountEnum status;
    @ManyToOne
    private Customer customer;

    @OneToMany (mappedBy = "bankAccount")
    private List<AccountOperation> accountOperations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public AccountEnum getStatus() {
        return status;
    }

    public void setStatus(AccountEnum status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<AccountOperation> getAccountOperations() {
        return accountOperations;
    }

    public void setAccountOperations(List<AccountOperation> accountOperations) {
        this.accountOperations = accountOperations;
    }
}