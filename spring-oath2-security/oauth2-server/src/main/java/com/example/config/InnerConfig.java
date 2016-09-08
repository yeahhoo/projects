package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author Aleksandr_Savchenko
 * */
@Configuration
public class InnerConfig {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public ApprovalStore approvalStore() throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore());
        return store;
    }

    @Bean
    public UserApprovalHandler userApprovalHandler() throws Exception {
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler() {

            @Override
            public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
                AuthorizationRequest result = super.updateAfterApproval(authorizationRequest, userAuthentication);
                if (!result.isApproved()) {
                    SecurityContextHolder.clearContext();
                }
                return result;
            }

        };
        handler.setTokenStore(tokenStore());
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);

        return handler;
    }

}
