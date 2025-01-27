package org.bp.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PopupController {

    @GetMapping("/showPopup")
    @ResponseBody
    public String showPopup(@RequestParam("orderId") String orderId, @RequestHeader(value = "requestSource", required = false) String requestSource, Model model) {
        if ("PizzaOrderingService".equals(requestSource)) {
            model.addAttribute("message", "Order ID: " + orderId);
            return "popup :: popupContent";
        }
        return "";
    }
}