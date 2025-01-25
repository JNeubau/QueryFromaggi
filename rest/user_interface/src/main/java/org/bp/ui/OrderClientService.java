package org.bp.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.bp.ui.model.order.OrderRequest;
import org.bp.ui.model.order.OrderResponse;

@Service
public class OrderClientService {

    @Autowired
    private RestTemplate restTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        String url = "http://localhost:8083/order";
        return restTemplate.postForObject(url, orderRequest, OrderResponse.class);
    }
}