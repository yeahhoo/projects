package com.example.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aleksandr_Savchenko
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OauthCreationDto implements Serializable {

    private static final long serialVersionUID = -778862342398333712L;

    //@JsonProperty(value = "client")
    private String client;
    //@JsonProperty(value = "secret")
    private String secret;
    //@JsonProperty(value = "grantTypes")
    private List<String> grantTypes;
    //@JsonProperty(value = "scopes")
    private List<String> scopes;



    public String getClient() {
        return client;
    }

    public String getSecret() {
        return secret;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public List<String> getScopes() {
        return scopes;
    }

    @Override
    public String toString() {
        return "OauthCreationDto{" +
               "client='" + client + '\'' +
               ", secret='" + secret + '\'' +
               ", grantTypes=" + grantTypes +
               ", scopes=" + scopes +
               '}';
    }
}
