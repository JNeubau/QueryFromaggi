package org.bp.payment.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PaymentResponse {
	@JsonProperty("transactionId")
	private int transactionId;

	@JsonProperty("transactionDate")
	private Date transactionDate;

	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PaymentResponse {\n");

		sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
		sb.append("    transactionDate: ").append(toIndentedString(transactionDate)).append("\n");
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
