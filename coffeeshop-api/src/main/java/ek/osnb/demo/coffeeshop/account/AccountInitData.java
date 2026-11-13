package ek.osnb.demo.coffeeshop.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class AccountInitData implements CommandLineRunner {

    private final AccountRepository accounts;
    private final PasswordEncoder passwordEncoder;
    private final String staffEmail;
    private final String staffPassword;

    AccountInitData(AccountRepository accounts, PasswordEncoder passwordEncoder,
                    @Value("${STAFF_EMAIL:}") String staffEmail,
                    @Value("${STAFF_PASSWORD:}") String staffPassword) {
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.staffEmail = staffEmail;
        this.staffPassword = staffPassword;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (staffEmail.isBlank() || staffPassword.isBlank()) {
            return;
        }
        if (accounts.existsByEmailIgnoreCase(staffEmail)) {
            return;
        }
        accounts.save(Account.createStaff(staffEmail, passwordEncoder.encode(staffPassword)));
    }
}