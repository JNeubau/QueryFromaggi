package org.bp.ui;

import org.bp.ui.model.payment.PaymentRequest;
import org.bp.ui.model.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.bp.ui.model.order.OrderRequest;
import org.bp.ui.model.order.OrderResponse;

@Service
public class ClientService {

    @Autowired
    private RestTemplate restTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        String url = "http://localhost:8083/order";
        return restTemplate.postForObject(url, orderRequest, OrderResponse.class);
    }

    public PaymentResponse payForOrder(PaymentRequest paymentRequest) {
        String url = "http://localhost:8083/payment";
        return restTemplate.postForObject(url, paymentRequest, PaymentResponse.class);
    }

    public PaymentResponse paymentStatus(String orderId) {
        String url = "http://localhost:8083/payment/" + orderId;
        return restTemplate.getForObject(url, PaymentResponse.class);
    }
}