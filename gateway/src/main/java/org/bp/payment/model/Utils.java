package org.bp.payment.model;

import org.bp.payment.model.order.OrderResponse;
import org.bp.payment.model.payment.PaymentResponse;

import java.math.BigDecimal;
import java.util.Date;

public class Utils {
	static public PizzaInfo preparePizzaInfo(String pizzaId, BigDecimal cost) {
		PizzaInfo pizzaInfo = new PizzaInfo();
		pizzaInfo.setId(pizzaId);
		pizzaInfo.setCost(cost);
		return pizzaInfo;
	}

	public static OrderResponse createOrderResponse() {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderId("1234");
		orderResponse.setOrderStatus("SUCCESSFUL ORDER");
		orderResponse.setOrderDescription("Order placed successfully");
		return orderResponse;
	}

	public static PaymentResponse createPaymentResponse() {
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setTransactionId(2137);
		paymentResponse.setTransactionDate(new Date());
		return paymentResponse;
	}

}
