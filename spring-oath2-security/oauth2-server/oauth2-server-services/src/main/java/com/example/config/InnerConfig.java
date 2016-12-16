package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author Aleksandr_Savchenko
 * */
@Configuration
@Import({DataSourceConfig.class})
public class InnerConfig {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() throws Exception {
        return new JdbcApprovalStore(dataSource);
    }

    // removes codes automatically while processing authentication
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        AuthorizationCodeServices codeServices = new JdbcAuthorizationCodeServices(dataSource);
        return codeServices;
    }

    @Bean
    public UserApprovalHandler userApprovalHandler() throws Exception {
        ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler() {

            @Override
            public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
                AuthorizationRequest result = super.updateAfterApproval(authorizationRequest, userAuthentication);
                if (!result.isApproved()) {
                    SecurityContextHolder.clearContext();
                }
                return result;
            }

        };
        handler.setApprovalStore(approvalStore());
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

}
