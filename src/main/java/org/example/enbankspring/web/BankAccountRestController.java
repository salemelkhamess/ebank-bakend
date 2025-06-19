package org.example.enbankspring.web;

import org.example.enbankspring.dtos.AccountHistoryDTO;
import org.example.enbankspring.dtos.AccountOperationDTO;
import org.example.enbankspring.dtos.BankAccountDTO;
import org.example.enbankspring.exceptions.BankAccountNotFoudException;
import org.example.enbankspring.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountRestController {
    private final BankAccountService bankAccountService;
    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(value = "/account/{id}")
    public BankAccountDTO getBankAccountById(@PathVariable String id) throws BankAccountNotFoudException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/account/history/{id}")
    public List<AccountOperationDTO> getHistoy(@PathVariable String id){
        return bankAccountService.accountHistory(id);
    }

    @GetMapping("/account/history/{id}/pages")
    public AccountHistoryDTO getAccountHistoy(@PathVariable String id , @RequestParam(name = "page" ,defaultValue = "0") int page , @RequestParam(name = "size" ,defaultValue = "5") int size) throws BankAccountNotFoudException {
        return bankAccountService.getAccountHistory(id ,page ,size);
    }


}
