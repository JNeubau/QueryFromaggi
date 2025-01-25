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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class PaymentController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/paymentForm")
    public String makePayment(@ModelAttribute PaymentRequest paymentRequest, Model model) {
        PaymentResponse paymentResponse = clientService.payForOrder(paymentRequest);
        model.addAttribute("paymentInfo", paymentResponse);
        return "result";
    }
}