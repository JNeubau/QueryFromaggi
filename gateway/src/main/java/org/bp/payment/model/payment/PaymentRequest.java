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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PaymentRequest {\n");

		sb.append("    paymentCard: ").append(toIndentedString(paymentCard)).append("\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
