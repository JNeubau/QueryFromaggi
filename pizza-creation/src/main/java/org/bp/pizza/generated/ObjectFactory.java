
package org.bp.pizza.generated;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.bp.pizza.generated.types.Fault;
import org.bp.pizza.generated.types.PizzaInfo;


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

    private final static QName _CreatePizzaResponse_QNAME = new QName("http://www.bp.org", "createPizzaResponse");
    private final static QName _PizzaFault_QNAME = new QName("http://www.bp.org", "pizzaFault");
    private final static QName _CancelPizzaResponse_QNAME = new QName("http://www.bp.org", "cancelPizzaResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreatePizzaRequest }
     * 
     */
    public CreatePizzaRequest createCreatePizzaRequest() {
        return new CreatePizzaRequest();
    }

    /**
     * Create an instance of {@link CancelPizzaRequest }
     * 
     */
    public CancelPizzaRequest createCancelPizzaRequest() {
        return new CancelPizzaRequest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PizzaInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PizzaInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "createPizzaResponse")
    public JAXBElement<PizzaInfo> createCreatePizzaResponse(PizzaInfo value) {
        return new JAXBElement<PizzaInfo>(_CreatePizzaResponse_QNAME, PizzaInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Fault }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "pizzaFault")
    public JAXBElement<Fault> createPizzaFault(Fault value) {
        return new JAXBElement<Fault>(_PizzaFault_QNAME, Fault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PizzaInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PizzaInfo }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.bp.org", name = "cancelPizzaResponse")
    public JAXBElement<PizzaInfo> createCancelPizzaResponse(PizzaInfo value) {
        return new JAXBElement<PizzaInfo>(_CancelPizzaResponse_QNAME, PizzaInfo.class, null, value);
    }

}
