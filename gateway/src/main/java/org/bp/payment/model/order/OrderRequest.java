package org.bp.payment.model.order;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bp.payment.model.delivery.Delivery;

/**
 * OrderRequest
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-06T08:44:40.322365400+01:00[Europe/Warsaw]")
public class OrderRequest {
  @JsonProperty("person")
  private Person person = null;

  @JsonProperty("order")
  private Pizza pizza = null;

  @JsonProperty("delivery")
  private Delivery delivery = null;

//  @JsonProperty("paymentCard")
//  private PaymentCard paymentCard = null;

  public OrderRequest person(Person person) {
    this.person = person;
    return this;
  }

   /**
   * Get person
   * @return person
  **/

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public OrderRequest hotel(Pizza pizza) {
    this.pizza = pizza;
    return this;
  }

   /**
   * Get hotel
   * @return hotel
  **/

  public Pizza getPizza() {
    return pizza;
  }

  public void setPizza(Pizza pizza) {
    this.pizza = pizza;
  }

  public OrderRequest flight(Delivery delivery) {
    this.delivery = delivery;
    return this;
  }

   /**
   * Get flight
   * @return flight
  **/

  public Delivery getDelivery() {
    return delivery;
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
  }

//  public OrderRequest paymentCard(PaymentCard paymentCard) {
//    this.paymentCard = paymentCard;
//    return this;
//  }

   /**
   * Get paymentCard
   * @return paymentCard
  **/

//  public PaymentCard getPaymentCard() {
//    return paymentCard;
//  }
//
//  public void setPaymentCard(PaymentCard paymentCard) {
//    this.paymentCard = paymentCard;
//  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderRequest orderRequest = (OrderRequest) o;
    return Objects.equals(this.person, orderRequest.person) &&
        Objects.equals(this.pizza, orderRequest.pizza) &&
        Objects.equals(this.delivery, orderRequest.delivery);
//        Objects.equals(this.paymentCard, orderRequest.paymentCard);
  }

  @Override
//  public int hashCode() {
//    return Objects.hash(person, hotel, flight, paymentCard);
//  }
  public int hashCode() {
    return Objects.hash(person, pizza, delivery);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderRequest {\n");
    
    sb.append("    person: ").append(toIndentedString(person)).append("\n");
    sb.append("    order: ").append(toIndentedString(pizza)).append("\n");
    sb.append("    delivery: ").append(toIndentedString(delivery)).append("\n");
//    sb.append("    paymentCard: ").append(toIndentedString(paymentCard)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
