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

	
}
