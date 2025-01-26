
package org.bp.payment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

public class OrderResponse {
  @JsonProperty("orderId")
  private String orderId = null;

  @JsonProperty("orderStatus")
  private String orderStatus = null;

  @JsonProperty("orderDescription")
  private String orderDescription = null;

  public OrderResponse orderId(String orderId) {
    this.orderId = orderId;
    return this;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public OrderResponse orderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public OrderResponse orderDescription(String orderDescription) {
    this.orderDescription = orderDescription;
    return this;
  }

  public String getOrderDescription() {
    return orderDescription;
  }

  public void setOrderDescription(String orderDescription) {
    this.orderDescription = orderDescription;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderResponse exceptionResponse = (OrderResponse) o;
    return Objects.equals(this.orderId, exceptionResponse.orderId) &&
        Objects.equals(this.orderStatus, exceptionResponse.orderStatus) &&
            Objects.equals(this.orderDescription, exceptionResponse.orderDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, orderStatus, orderDescription);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderResponse {\n");
    
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    orderStatus: ").append(toIndentedString(orderStatus)).append("\n");
    sb.append("    orderDescription: ").append(toIndentedString(orderDescription)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
