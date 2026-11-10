package ek.osnb.demo.coffeeshop.account;

class AccountMapper {

    static AccountResponse toResponse(Account account){
        return new AccountResponse(
                account.getId(),
                account.getEmail(),
                account.getRole());
    }
}
