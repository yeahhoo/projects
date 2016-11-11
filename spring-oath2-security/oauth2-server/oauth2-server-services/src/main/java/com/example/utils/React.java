package com.example.utils;

/**
 * @author Aleksandr_Savchenko
 */
public class React {

    public String renderServerLogoutForm(String user, String secretToken, String tokenToReturn, String csrfToken) {
        try {
            Object html = ReactEngineSingleton.getReactEngine().invokeFunction("renderServerLogoutForm", user, secretToken, tokenToReturn, csrfToken);
            return String.valueOf(html);
        } catch (Exception e) {
            throw new IllegalStateException("failed to render react component", e);
        }
    }


}
