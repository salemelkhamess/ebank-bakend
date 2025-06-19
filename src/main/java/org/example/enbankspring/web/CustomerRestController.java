package org.example.enbankspring.web;

import org.example.enbankspring.dtos.CustomerDTO;
import org.example.enbankspring.exceptions.CustomerNotFoundException;
import org.example.enbankspring.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/save")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.createCustomer(customerDTO);
    }

    @PutMapping("/update/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id , @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/delet/{id}")
    public  void deletCustomer(@PathVariable Long id) {
        bankAccountService.deletCustomer(id);
    }
}
