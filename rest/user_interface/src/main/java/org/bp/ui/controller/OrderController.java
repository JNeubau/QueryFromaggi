package org.bp.ui.controller;

import org.bp.ui.ClientService;
import org.bp.ui.model.order.*;
import org.bp.ui.model.payment.Amount;
import org.bp.ui.model.payment.PaymentCard;
import org.bp.ui.model.payment.PaymentRequest;
import org.bp.ui.model.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class OrderController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/orderForm")
    public String showOrderForm(Model model) {
        model.addAttribute("orderRequest", new OrderRequest());
        return "order";
    }

    @PostMapping("/orderForm")
    public String makeOrder(@ModelAttribute OrderRequest orderRequest, Model model) {
        if (orderRequest.getPizza() == null) {
            orderRequest.setPizza(new Pizza());
        }
        if (orderRequest.getDelivery() == null) {
            orderRequest.setDelivery(new Delivery());
        }
        if (orderRequest.getDelivery().getFrom() == null) {
            orderRequest.getDelivery().setFrom(new Point());
        }
        if (orderRequest.getDelivery().getTo() == null) {
            orderRequest.getDelivery().setTo(new Point());
        }
        if (orderRequest.getPaymentRequest() == null) {
            orderRequest.setPaymentRequest(new PaymentRequest());
        }
        if (orderRequest.getPaymentRequest().getAmount() == null) {
            orderRequest.getPaymentRequest().setAmount(new Amount());
        }
        if (orderRequest.getPaymentRequest().getPaymentCard() == null) {
            orderRequest.getPaymentRequest().setPaymentCard(new PaymentCard());
        }

        BigDecimal newPrize = BigDecimal.valueOf(orderRequest.getPizza().getSize() + 20
                + orderRequest.getPizza().getIngredients().length() * 3);
        orderRequest.getPizza().setPrize(newPrize);

        orderRequest.getPaymentRequest().getAmount().setValue(newPrize);

        OffsetDateTime now = OffsetDateTime.now();
        int randomMinutes = ThreadLocalRandom.current().nextInt(30, 61);
        OffsetDateTime prepTime = now.plusMinutes(randomMinutes);
        orderRequest.getPizza().setPrepTime(prepTime);

        orderRequest.getDelivery().getFrom().setDate(prepTime);
        orderRequest.getDelivery().getTo().setDate(prepTime.plusMinutes(30));

        OrderResponse orderResponse = clientService.placeOrder(orderRequest);

        model.addAttribute("orderId", orderResponse.getOrderId());
        return "result";
    }


    @GetMapping("/cancelOrder")
    public String cancelOrder(@ModelAttribute OrderRequest orderRequest, Model model) {
        return "redirect:/orderForm";
    }
}