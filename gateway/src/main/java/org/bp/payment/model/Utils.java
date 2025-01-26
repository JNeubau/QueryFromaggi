package org.bp.payment.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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

}
