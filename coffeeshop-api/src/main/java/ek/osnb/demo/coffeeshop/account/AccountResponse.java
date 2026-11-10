package ek.osnb.demo.coffeeshop.account;

public record AccountResponse(
        Long id,
        String email,
        AccountRole role
) {
}
