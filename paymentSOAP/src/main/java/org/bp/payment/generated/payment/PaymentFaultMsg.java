
package org.bp.payment.generated.payment;


import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.3.2
 * 2025-01-18T21:22:31.652+01:00
 * Generated source version: 3.3.2
 */

@WebFault(name = "paymentFault", targetNamespace = "http://www.bp.org")
public class PaymentFaultMsg extends Exception {

    private org.bp.payment.generated.types.Fault paymentFault;

    public PaymentFaultMsg() {
        super();
    }

    public PaymentFaultMsg(String message) {
        super(message);
    }

    public PaymentFaultMsg(String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public PaymentFaultMsg(String message, org.bp.payment.generated.types.Fault paymentFault) {
        super(message);
        this.paymentFault = paymentFault;
    }

    public PaymentFaultMsg(String message, org.bp.payment.generated.types.Fault paymentFault, java.lang.Throwable cause) {
        super(message, cause);
        this.paymentFault = paymentFault;
    }

    public org.bp.payment.generated.types.Fault getFaultInfo() {
        return this.paymentFault;
    }
}