package org.bp.ui.controller;

import org.bp.ui.model.payment.PaymentRequest;
import org.bp.ui.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/paymentForm")
    public String showPaymentForm(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequest());
        return "payment";
    }

    @PostMapping("/paymentForm")
    public String makePayment(@ModelAttribute PaymentRequest paymentRequest, Model model) {
        paymentService.payment(paymentRequest);
        model.addAttribute("paymentInfo", paymentRequest);
        return "result";
    }
}