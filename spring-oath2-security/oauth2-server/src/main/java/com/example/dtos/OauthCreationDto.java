package com.example.dtos;

import java.io.Serializable;
import java.util.List;

/**
 *
 * http://www.jworks.nl/2013/08/21/register-a-custom-jackson-objectmapper-using-spring-javaconfig/
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
