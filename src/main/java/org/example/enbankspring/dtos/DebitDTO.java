package org.example.enbankspring.dtos;

import lombok.Data;

@Data
public class DebitDTO {

    private String accountID;
    private double amount;
    private String description;

}
