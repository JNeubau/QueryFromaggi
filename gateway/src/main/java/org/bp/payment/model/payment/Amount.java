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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Amount {\n");

		sb.append("    value: ").append(toIndentedString(value)).append("\n");
		sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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
