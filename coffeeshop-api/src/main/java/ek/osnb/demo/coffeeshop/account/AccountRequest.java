package ek.osnb.demo.coffeeshop.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record AccountRequest(
        @NotBlank
        @Size(max = Account.MAX_LENGTH_EMAIL)
        String email,
        @NotBlank
        String password
) {
}
