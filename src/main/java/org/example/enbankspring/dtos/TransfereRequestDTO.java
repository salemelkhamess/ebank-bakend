package org.example.enbankspring.dtos;

import lombok.Data;

@Data
public class TransfereRequestDTO {

    private String accountSource;
    private String accountDest;
    private double amount;


}
