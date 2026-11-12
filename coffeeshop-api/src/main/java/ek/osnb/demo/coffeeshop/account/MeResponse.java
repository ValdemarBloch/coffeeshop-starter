package ek.osnb.demo.coffeeshop.account;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

record MeResponse(
        @NotBlank
        String username,
        @NotBlank
        List<String> roles) {
}
