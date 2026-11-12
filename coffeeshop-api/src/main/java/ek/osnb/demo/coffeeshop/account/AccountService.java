package ek.osnb.demo.coffeeshop.account;

import ek.osnb.demo.coffeeshop.common.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    AccountService(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.accountRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    AccountResponse register(AccountRequest request){
        String passwordHashed = passwordEncoder.encode(request.password());
        Account account = Account.registerAccount(
                request.email(),
                passwordHashed);

        return AccountMapper.toResponse(accountRepository.save(account));
    }
    MeResponse findMe(String email){
        return accountRepository.findByEmailIgnoreCase(email)
                .map(account -> new MeResponse(account.getEmail(),
                        List.of("ROLE_"+account.getRole().name())))
                .orElseThrow(()-> new NotFoundException("No account with the email "+email+" exist"));
    }
}
