package org.bp.payment;


import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.bp.payment.model.order.OrderRequest;
import org.bp.payment.model.PizzaInfo;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	private HashMap<String, PaymentData> payments;
	
	@PostConstruct
	void init() {
		payments=new HashMap<>();
	}
	
	public static class PaymentData {
		OrderRequest orderRequest;
		PizzaInfo pizzaPizzaInfo;
		PizzaInfo deliveryPizzaInfo;
		boolean finished;
		public boolean isReady() {
			return orderRequest !=null && pizzaPizzaInfo !=null && deliveryPizzaInfo !=null;
		}
	}
	
	public synchronized boolean addPizzaOrderRequest(String pizzaId, OrderRequest orderRequest) {
		PaymentData paymentData = getPaymentData(pizzaId);
		paymentData.orderRequest = orderRequest;
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

	public synchronized boolean checkPaymentData(String orderId) {
		PaymentData paymentData = payments.get(orderId);
		if (paymentData == null) {
			return false;
		} else {
			return true;
		}
	}

}
