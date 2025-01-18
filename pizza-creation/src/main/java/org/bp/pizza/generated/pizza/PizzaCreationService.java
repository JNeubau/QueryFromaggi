package org.bp.pizza.generated.pizza;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.3.2
 * 2025-01-18T20:07:24.100+01:00
 * Generated source version: 3.3.2
 *
 */
@WebServiceClient(name = "PizzaCreationService",
                  wsdlLocation = "file:/C:/Users/neuba/Documents/semestr9/AZNU/QueryFromaggi/pizza-creation/src/main/resources/pizza.wsdl",
                  targetNamespace = "http://www.bp.org/pizza/")
public class PizzaCreationService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.bp.org/pizza/", "PizzaCreationService");
    public final static QName PizzaCreationPort = new QName("http://www.bp.org/pizza/", "PizzaCreationPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/neuba/Documents/semestr9/AZNU/QueryFromaggi/pizza-creation/src/main/resources/pizza.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PizzaCreationService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/neuba/Documents/semestr9/AZNU/QueryFromaggi/pizza-creation/src/main/resources/pizza.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public PizzaCreationService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PizzaCreationService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PizzaCreationService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public PizzaCreationService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public PizzaCreationService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public PizzaCreationService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns PizzaCreation
     */
    @WebEndpoint(name = "PizzaCreationPort")
    public PizzaCreation getPizzaCreationPort() {
        return super.getPort(PizzaCreationPort, PizzaCreation.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PizzaCreation
     */
    @WebEndpoint(name = "PizzaCreationPort")
    public PizzaCreation getPizzaCreationPort(WebServiceFeature... features) {
        return super.getPort(PizzaCreationPort, PizzaCreation.class, features);
    }

}