package org.bp.pizza.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PizzaOrderRequest
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-06T08:44:40.322365400+01:00[Europe/Warsaw]")
public class PizzaOrderRequest {
  @JsonProperty("person")
  private Person person = null;

  @JsonProperty("pizza")
  private Pizza pizza = null;

  @JsonProperty("delivery")
  private Delivery delivery = null;

//  @JsonProperty("paymentCard")
//  private PaymentCard paymentCard = null;

  public PizzaOrderRequest person(Person person) {
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

  public PizzaOrderRequest hotel(Pizza pizza) {
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

  public PizzaOrderRequest flight(Delivery delivery) {
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

//  public PizzaOrderRequest paymentCard(PaymentCard paymentCard) {
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
    PizzaOrderRequest pizzaOrderRequest = (PizzaOrderRequest) o;
    return Objects.equals(this.person, pizzaOrderRequest.person) &&
        Objects.equals(this.pizza, pizzaOrderRequest.pizza) &&
        Objects.equals(this.delivery, pizzaOrderRequest.delivery);
//        Objects.equals(this.paymentCard, pizzaOrderRequest.paymentCard);
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
    sb.append("class PizzaOrderRequest {\n");
    
    sb.append("    person: ").append(toIndentedString(person)).append("\n");
    sb.append("    pizza: ").append(toIndentedString(pizza)).append("\n");
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
