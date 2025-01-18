package org.bp.payment.model;

public class Utils {
	static public PizzaInfo preparePizzaInfo(String pizzaId, Float cost) {
		PizzaInfo pizzaInfo = new PizzaInfo();
		pizzaInfo.setId(pizzaId);
		pizzaInfo.setCost(cost);
		return pizzaInfo;
	}

}
