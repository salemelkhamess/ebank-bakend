package org.example.enbankspring.web;

import org.example.enbankspring.dtos.CustomerDTO;
import org.example.enbankspring.entities.Customer;
import org.example.enbankspring.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    public CustomerRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/list")
    public List<CustomerDTO> getCustomers() {

        return bankAccountService.listCustomers();
    }
}
