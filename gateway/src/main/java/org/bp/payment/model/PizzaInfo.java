/*
 * Travel mircro service
 * Micro service to book a travel
 *
 * OpenAPI spec version: 1.0.0
 * Contact: supportm@bp.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.bp.payment.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PizzaInfo
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-12-06T08:44:40.322365400+01:00[Europe/Warsaw]")
public class PizzaInfo {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("cost")
  private Float cost = null;

  public PizzaInfo id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PizzaInfo cost(Float cost) {
    this.cost = cost;
    return this;
  }

   /**
   * Get cost
   * @return cost
  **/

  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PizzaInfo pizzaInfo = (PizzaInfo) o;
    return Objects.equals(this.id, pizzaInfo.id) &&
        Objects.equals(this.cost, pizzaInfo.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cost);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PizzaInfo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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