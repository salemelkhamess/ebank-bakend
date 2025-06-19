package org.example.enbankspring.dtos;

import java.util.List;

public class AccountHistoryDTO {

    private String accountId;
    private double balance;
    private int currentPages;
    private int totalPages;
    private int size;

    private List<AccountOperationDTO> accountOperationDTOS;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getCurrentPages() {
        return currentPages;
    }

    public void setCurrentPages(int currentPages) {
        this.currentPages = currentPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<AccountOperationDTO> getAccountOperationDTOS() {
        return accountOperationDTOS;
    }

    public void setAccountOperationDTOS(List<AccountOperationDTO> accountOperationDTOS) {
        this.accountOperationDTOS = accountOperationDTOS;
    }
}
