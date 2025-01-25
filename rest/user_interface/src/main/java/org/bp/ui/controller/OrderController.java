package org.bp.ui.controller;

import org.bp.ui.OrderClientService;
import org.bp.ui.model.order.*;
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
    private OrderClientService orderClientService;

    @GetMapping("/orderForm")
    public String showOrderForm(Model model) {
        model.addAttribute("orderRequest", new OrderRequest());
        return "order";
    }

//    @PostMapping("/orderForm")
//    public String createOrderForm(@ModelAttribute OrderRequest orderRequest, Model model) {
//        OrderResponse orderResponse = orderService.order(orderRequest);
//        model.addAttribute("orderInfo", orderResponse);
//        return "orderConfirmation";
//    }

//    @GetMapping("/orderConfirmation")
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

        orderRequest.getPizza().setPrize(BigDecimal.valueOf(orderRequest.getPizza().getSize() + 20));

        OffsetDateTime now = OffsetDateTime.now();
        int randomMinutes = ThreadLocalRandom.current().nextInt(30, 61);
        OffsetDateTime prepTime = now.plusMinutes(randomMinutes);
        orderRequest.getPizza().setPrepTime(prepTime);

        orderRequest.getDelivery().getFrom().setDate(prepTime);
        orderRequest.getDelivery().getTo().setDate(prepTime.plusMinutes(30));

        OrderResponse orderResponse = orderClientService.placeOrder(orderRequest);

        model.addAttribute("orderRequest", orderRequest);
        model.addAttribute("orderResponse", orderResponse);
        return "orderConfirmation";
    }

//    @PostMapping("/orderForm")
//    public String createOrderForm(@ModelAttribute OrderRequest orderRequest, Model model) {
//        OrderResponse orderResponse = orderClientService.placeOrder(orderRequest);
//        model.addAttribute("orderInfo", orderResponse);
//        return "orderConfirmation";
//    }

    @GetMapping("/cancelOrder")
    public String cancelOrder(@ModelAttribute OrderRequest orderRequest, Model model) {
        return "redirect:/orderForm";
    }
}