package org.bp.payment.generated.payment;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.3.2
 * 2025-01-18T21:22:31.678+01:00
 * Generated source version: 3.3.2
 *
 */
@WebService(targetNamespace = "http://www.bp.org/payment/", name = "PaymentCreation")
@XmlSeeAlso({org.bp.payment.generated.ObjectFactory.class, org.bp.payment.generated.types.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface PaymentCreation {

    @WebMethod(action = "http://www.bp.org/pizza/makePayment")
    @WebResult(name = "makePaymentResponse", targetNamespace = "http://www.bp.org", partName = "payload")
    public org.bp.payment.generated.types.PaymentInfo makePayment(

        @WebParam(partName = "payload", name = "makePaymentRequest", targetNamespace = "http://www.bp.org")
        org.bp.payment.generated.MakePaymentRequest payload
    ) throws PaymentFaultMsg;

    @WebMethod(action = "http://www.bp.org/pizza/cancelPayment")
    @WebResult(name = "cancelPaymentResponse", targetNamespace = "http://www.bp.org", partName = "payload")
    public org.bp.payment.generated.types.PaymentInfo cancelPayment(

        @WebParam(partName = "payload", name = "cancelPaymentRequest", targetNamespace = "http://www.bp.org")
        org.bp.payment.generated.CancelPaymentRequest payload
    ) throws PaymentFaultMsg;
}
