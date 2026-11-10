package ek.osnb.demo.coffeeshop.account;

import org.springframework.stereotype.Service;

@Service
class AccountService {
    private final AccountRepository repository;

    AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    AccountResponse register(AccountRequest request){
        Account account = Account.registerAccount(
                request.email(),
                request.password());

        return AccountMapper.toResponse(repository.save(account));
    }
}
