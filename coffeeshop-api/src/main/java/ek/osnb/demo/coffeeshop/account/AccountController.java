package ek.osnb.demo.coffeeshop.account;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    AccountResponse register(@RequestBody AccountRequest request) {
        return accountService.register(request);
    }

    @GetMapping("/me")
    MeResponse returnMe(Authentication auth) {
        return accountService.findMe(auth.getName());
    }

}

