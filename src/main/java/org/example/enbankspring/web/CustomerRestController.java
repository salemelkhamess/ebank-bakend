package org.example.enbankspring.web;

import org.example.enbankspring.dtos.CustomerDTO;
import org.example.enbankspring.exceptions.CustomerNotFoundException;
import org.example.enbankspring.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerRestController {

    private BankAccountService bankAccountService;
    public CustomerRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public List<CustomerDTO> getCustomers() {
        return bankAccountService.listCustomers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")

    public CustomerDTO getCustomer(@PathVariable Long id) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.createCustomer(customerDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    public CustomerDTO updateCustomer(@PathVariable Long id , @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(id);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/delet/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    public  void deletCustomer(@PathVariable Long id) {
        bankAccountService.deletCustomer(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_USER' , 'ROLE_ADMIN')")

    public List<CustomerDTO> searchCustomer(@RequestParam(name = "keyword", defaultValue = "") String search) {
        return bankAccountService.searchCustomer("%"+search+"%");
    }

}
