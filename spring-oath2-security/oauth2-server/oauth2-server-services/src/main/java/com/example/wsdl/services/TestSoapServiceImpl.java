package com.example.wsdl.services;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * {@inheritDoc}
 * @author Aleksandr_Savchenko
 */
@WebService(endpointInterface = "com.example.wsdl.services.TestSoapService")
public class TestSoapServiceImpl implements TestSoapService {

    @Override
    public String hi(@WebParam String name) {
        return "Hello: " + name;
    }
}
