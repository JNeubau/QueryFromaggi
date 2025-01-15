package org.bp.pizza.model;

import java.math.BigDecimal;

public class Utils {
	static public PizzaInfo preparePizzaInfo(String pizzaId, BigDecimal cost) {
		PizzaInfo pizzaInfo = new PizzaInfo();
		pizzaInfo.setId(pizzaId);
		pizzaInfo.setCost(cost);
		return pizzaInfo;
	}

}
