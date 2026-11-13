package ek.osnb.demo.coffeeshop.account;


import ek.osnb.demo.coffeeshop.common.InvalidInputException;
import jakarta.persistence.*;

import java.util.Locale;

@Entity
class Account {
    static final int MAX_LENGTH_EMAIL = 254;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true, length = MAX_LENGTH_EMAIL )
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    protected Account() {
    }

     private Account(String email, String passwordHash, AccountRole role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    static Account registerAccount(String email,String passwordHashed){
        String normalizedEmail = normalizeEmail(requiredEmail(email,"email"));
        String ValidPassword = requiredPassword(passwordHashed,"password");
        return new Account(normalizedEmail,ValidPassword,AccountRole.CUSTOMER);
    }

    static Account createStaff(String email, String passwordHash) {
        String normalizedEmail = normalizeEmail(requiredEmail(email, "email"));
        String validPassword = requiredPassword(passwordHash, "password");
        return new Account(normalizedEmail, validPassword, AccountRole.STAFF);
    }

    private static String requiredEmail(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException("A account must have a " + field);
        }
        return value.trim();
    }
    static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
    private static String requiredPassword(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException("A account must have a " + field);
        }
        return value.trim();
    }

    String getEmail() {
        return email;
    }

    String getPasswordHash() {
        return passwordHash;
    }

    AccountRole getRole() {
        return role;
    }
    Long getId() {
        return accountId;
    }
}




