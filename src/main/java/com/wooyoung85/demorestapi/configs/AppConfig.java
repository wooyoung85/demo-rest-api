package com.wooyoung85.demorestapi.configs;

import com.wooyoung85.demorestapi.accounts.Account;
import com.wooyoung85.demorestapi.accounts.AccountRepository;
import com.wooyoung85.demorestapi.accounts.AccountRole;
import com.wooyoung85.demorestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account wooyoung = Account.builder()
                    .email(appProperties.getAdminUsername())
                    .password(appProperties.getAdminPassword())
                    .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                    .build();

                accountService.saveAccount(wooyoung);

                Account wooyoung1 = Account.builder()
                    .email(appProperties.getUserUsername())
                    .password(appProperties.getUserPassword())
                    .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                    .build();

                accountService.saveAccount(wooyoung1);
            }
        };
    }
}
