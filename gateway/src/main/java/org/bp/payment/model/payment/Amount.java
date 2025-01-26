package org.bp.payment.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Amount {
	@JsonProperty("value")
	private BigDecimal value;

	@JsonProperty("currency")
	private String currency;

	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	

}
