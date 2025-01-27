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

	public static OrderResponse createOrderResponse(String pizzaId) {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderId(pizzaId);
		orderResponse.setOrderStatus("SUCCESSFUL ORDER");
		orderResponse.setOrderDescription("Order placed successfully");
		return orderResponse;
	}

	public static OrderResponse createErrorOrderResponse(String pizzaId, String msg) {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderId(pizzaId);
		orderResponse.setOrderStatus("ERROR ORDER");
		orderResponse.setOrderDescription("Order unsuccessful: " + msg);
		return orderResponse;
	}

	public static PaymentResponse createPaymentResponse(int paymentStatus) {
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setTransactionId(paymentStatus);
		paymentResponse.setTransactionDate(new Date());
		return paymentResponse;
	}
}
