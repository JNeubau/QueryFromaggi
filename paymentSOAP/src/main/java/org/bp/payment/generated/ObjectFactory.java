
package org.bp.payment.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.bp.payment.generated.types.Fault;
import org.bp.payment.generated.types.PaymentInfo;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bp package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MakePaymentResponse_QNAME = new QName("http://www.bp.org", "makePaymentResponse");
    private final static QName _PaymentFault_QNAME = new QName("http://www.bp.org", "paymentFault");
    private final static QName _CancelPaymentResponse_QNAME = new QName("http://www.bp.org", "cancelPaymentResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MakePaymentRequest }
     * 
     */
    public MakePaymentRequest createMakePaymentRequest() {
        return new MakePaymentRequest();
    }

    /**
     * Create an instance of {@link CancelPaymentRequest }
     * 
     */
    public CancelPaymentRequest createCancelPaymentRequest() {
        return new CancelPaymentRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PaymentInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "makePaymentResponse")
    public JAXBElement<PaymentInfo> createMakePaymentResponse(PaymentInfo value) {
        return new JAXBElement<PaymentInfo>(_MakePaymentResponse_QNAME, PaymentInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "paymentFault")
    public JAXBElement<Fault> createPaymentFault(Fault value) {
        return new JAXBElement<Fault>(_PaymentFault_QNAME, Fault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PaymentInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "cancelPaymentResponse")
    public JAXBElement<PaymentInfo> createCancelPaymentResponse(PaymentInfo value) {
        return new JAXBElement<PaymentInfo>(_CancelPaymentResponse_QNAME, PaymentInfo.class, null, value);
    }

}
