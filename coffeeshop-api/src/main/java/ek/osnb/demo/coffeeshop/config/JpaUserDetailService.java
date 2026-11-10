package ek.osnb.demo.coffeeshop.config;


import ek.osnb.demo.coffeeshop.account.Account;
import ek.osnb.demo.coffeeshop.account.AccountRepository;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    JpaUserDetailService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException{
        Account account = accountRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("No account for " + username));

        return User.withUsername(account.getEmail())
                .password(account.getPasswordHash())
                .roles(account.getRole().name())
                .build();
    }


}
