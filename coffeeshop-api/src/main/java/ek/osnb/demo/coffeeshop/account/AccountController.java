package ek.osnb.demo.coffeeshop.account;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    AccountResponse register(@RequestBody AccountRequest request){
        return accountService.register(request);
    }

    @GetMapping("/me")
    MeResponse returnMe(java.security.Principal principal){
        return accountService.findMe(principal.getName());
    }
}

