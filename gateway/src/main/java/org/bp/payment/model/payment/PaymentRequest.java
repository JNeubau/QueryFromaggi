package org.bp.payment.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {
	@JsonProperty("paymentCard")
	private PaymentCard paymentCard;

	@JsonProperty("amount")
	private Amount amount;

	public PaymentCard getPaymentCard() {
		return paymentCard;
	}
	public void setPaymentCard(PaymentCard paymentCard) {
		this.paymentCard = paymentCard;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}

}
