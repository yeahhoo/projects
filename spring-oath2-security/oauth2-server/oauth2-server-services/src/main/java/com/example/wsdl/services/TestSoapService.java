package com.example.wsdl.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


/**
 * "Hello World" WSDL service just to combine REST and WSDL. It has no actual meaning.
 * @author Aleksandr_Savchenko
 */
@WebService
public interface TestSoapService {

    @WebMethod(operationName = "helloWorld", action = "/soap-hello/hi")
    String hi(final @WebParam String name);

}
