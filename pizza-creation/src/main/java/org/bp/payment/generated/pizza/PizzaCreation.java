package org.bp.payment.generated.pizza;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.3.2
 * 2025-01-18T20:07:24.083+01:00
 * Generated source version: 3.3.2
 *
 */
@WebService(targetNamespace = "http://www.bp.org/pizza/", name = "PizzaCreation")
@XmlSeeAlso({org.bp.payment.generated.ObjectFactory.class, org.bp.payment.generated.types.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PizzaCreation {

    @WebMethod(action = "http://www.bp.org/pizza/cancelPizza")
    @WebResult(name = "cancelPizzaResponse", targetNamespace = "http://www.bp.org", partName = "payload")
    public org.bp.payment.generated.types.PizzaInfo cancelPizza(

        @WebParam(partName = "payload", name = "cancelPizzaRequest", targetNamespace = "http://www.bp.org")
        org.bp.payment.generated.CancelPizzaRequest payload
    ) throws PizzaFaultMsg;

    @WebMethod(action = "http://www.bp.org/pizza/createPizza")
    @WebResult(name = "createPizzaResponse", targetNamespace = "http://www.bp.org", partName = "payload")
    public org.bp.payment.generated.types.PizzaInfo createPizza(

        @WebParam(partName = "payload", name = "createPizzaRequest", targetNamespace = "http://www.bp.org")
        org.bp.payment.generated.CreatePizzaRequest payload
    ) throws PizzaFaultMsg;
}
