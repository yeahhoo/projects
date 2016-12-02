package com.example.wsdl.services;

import javax.jws.WebParam;
import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 * @author Aleksandr_Savchenko
 */
@WebService(endpointInterface = "com.example.wsdl.services.TestSoapService")
public class TestSoapServiceImpl implements TestSoapService {

    private static final Logger logger = LoggerFactory.getLogger(TestSoapServiceImpl.class);

    @Override
    public String hi(@WebParam String name) {
        logger.info("TestSoapServiceImpl was called with the param: {}", name);
        return "Hello: " + name;
    }
}
