package org.bp.ui.controller;

import org.bp.ui.ClientService;
import org.bp.ui.model.order.OrderRequest;
import org.bp.ui.model.order.OrderResponse;
import org.bp.ui.model.payment.Amount;
import org.bp.ui.model.payment.PaymentCard;
import org.bp.ui.model.payment.PaymentRequest;
import org.bp.ui.PaymentService;
import org.bp.ui.model.payment.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class PaymentController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/paymentForm/{orderId}")
    public String makePayment(@PathVariable String orderId, Model model) {
        System.out.println("Order ID peymentForm: " + orderId);
        PaymentResponse paymentResponse = clientService.paymentStatus(orderId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("paymentInfo", paymentResponse);
        return "resultStatus";
    }

    @GetMapping("/result/{orderId}")
    public String getResult(@PathVariable String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "result";
    }
}