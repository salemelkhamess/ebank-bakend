package org.example.enbankspring.dtos;

import org.example.enbankspring.enums.AccountEnum;

import java.util.Date;


public class SavingBankAccountDTO  extends BankAccountDTO {

    public String id;
    private double balance;
    private Date createdAt;
    private AccountEnum status;
    private CustomerDTO customerDTO;
    private  double interestRate;

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


    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }
}