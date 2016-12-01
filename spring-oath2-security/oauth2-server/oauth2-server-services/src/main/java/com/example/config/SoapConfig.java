package com.example.config;

import com.example.wsdl.services.TestSoapServiceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author Aleksandr_Savchenko
 */
@Configuration
public class SoapConfig {

    @Value("${server.wsdl-port}")
    private String servicePort;

    @Bean
    public Endpoint jaxwsTestSoapEndpoint(){
        return Endpoint.publish("http://localhost:" + servicePort + "/server/soap-hello", new TestSoapServiceImpl());
    }

}
