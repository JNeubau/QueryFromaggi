package org.bp.pizza;


import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.bp.pizza.model.PizzaOrderRequest;
import org.bp.pizza.model.PizzaInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	private HashMap<String, PaymentData> payments;
	
	@PostConstruct
	void init() {
		payments=new HashMap<>();
	}
	
	public static class PaymentData {
		PizzaOrderRequest pizzaOrderRequest;
		PizzaInfo pizzaPizzaInfo;
		PizzaInfo deliveryPizzaInfo;
		public boolean isReady() {
			return pizzaOrderRequest !=null && pizzaPizzaInfo !=null && deliveryPizzaInfo !=null;
		}
	}
	
	public synchronized boolean addPizzaOrderRequest(String pizzaId, PizzaOrderRequest pizzaOrderRequest) {
		PaymentData paymentData = getPaymentData(pizzaId);
		paymentData.pizzaOrderRequest = pizzaOrderRequest;
		return paymentData.isReady();
	}
	

	public synchronized boolean addPizzaInfo(String pizzaId, PizzaInfo pizzaInfo, String serviceType) {
		PaymentData paymentData = getPaymentData(pizzaId);
		if (serviceType.equals("delivery"))
			paymentData.deliveryPizzaInfo = pizzaInfo;
		else 
			paymentData.pizzaPizzaInfo = pizzaInfo;
		return paymentData.isReady();
	}	
	
	
	public synchronized PaymentData getPaymentData(String bookTravelId) {
		PaymentData paymentData = payments.get(bookTravelId);
		if (paymentData == null) {
			paymentData = new PaymentData();
			payments.put(bookTravelId, paymentData);
		}
		return paymentData;
	}

	


	

}
