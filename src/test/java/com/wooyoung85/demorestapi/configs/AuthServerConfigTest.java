package com.wooyoung85.demorestapi.configs;

import com.wooyoung85.demorestapi.accounts.Account;
import com.wooyoung85.demorestapi.accounts.AccountRole;
import com.wooyoung85.demorestapi.accounts.AccountService;
import com.wooyoung85.demorestapi.common.BaseControllerTest;
import com.wooyoung85.demorestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        this.mockMvc.perform(post("/oauth/token")
            .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
            .param("username", appProperties.getUserUsername())
            .param("password", appProperties.getUserPassword())
            .param("grant_type", "password")
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("access_token").exists());
    }
}