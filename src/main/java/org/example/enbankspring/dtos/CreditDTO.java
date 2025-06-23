package org.example.enbankspring.dtos;

import lombok.Data;

@Data
public class CreditDTO {

    private String accountID;
    private double amount;
    private String description;

}
