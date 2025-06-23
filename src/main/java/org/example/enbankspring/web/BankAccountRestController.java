package org.example.enbankspring.web;

import org.example.enbankspring.dtos.*;
import org.example.enbankspring.exceptions.AccountBalanceInsiffisantException;
import org.example.enbankspring.exceptions.BankAccountNotFoudException;
import org.example.enbankspring.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
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

    @PostMapping("/account/debiter")
    public DebitDTO debiter(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoudException, AccountBalanceInsiffisantException {
        bankAccountService.debit(debitDTO.getAccountID() ,debitDTO.getDescription(),debitDTO.getAmount());
        return debitDTO;
    }

    @PostMapping("/account/crediter")
    public CreditDTO crediter(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoudException, AccountBalanceInsiffisantException {
        bankAccountService.credit(creditDTO.getAccountID() ,creditDTO.getDescription(),creditDTO.getAmount());
        return creditDTO;
    }


    @PostMapping("/account/virement")
    public void virement(@RequestBody TransfereRequestDTO transfereRequestDTO) throws BankAccountNotFoudException, AccountBalanceInsiffisantException {
        this.bankAccountService.transfer(transfereRequestDTO.getAccountSource() , transfereRequestDTO.getAccountDest(),transfereRequestDTO.getAmount());
    }

}
