package ek.osnb.demo.coffeeshop.account;

import java.util.List;

public record MeResponse(String username, List<String> roles) {
}
