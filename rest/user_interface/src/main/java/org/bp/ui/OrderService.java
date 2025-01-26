package org.bp.ui;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.camel.ProducerTemplate;
import org.bp.ui.model.order.UiException;
import org.bp.ui.model.order.OrderRequest;
import org.bp.ui.model.order.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@org.springframework.web.bind.annotation.RestController
//@Controller
@OpenAPIDefinition(info = @Info(
        title = "Order service",
        version = "1",
        description = "Service for ordering pizza and delivery"))

public class OrderService {

	@Autowired
	private ProducerTemplate producerTemplate;

	@Autowired
	private ObjectMapper objectMapper; // Add this line

	@PostMapping("/order")
	@Operation(
			summary = "ordering operation",
			description = "operation for order",
			responses = {
				@ApiResponse(responseCode = "200",
						description = "OK",
						content = {@Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))}),
				@ApiResponse(responseCode = "400", description = "Bad Request",
						content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UiException.class))})
			})
	public OrderResponse order(
		@org.springframework.web.bind.annotation.RequestBody OrderRequest orderRequest) {
		if (orderRequest !=null && orderRequest.getPizza()!=null
			&& orderRequest.getPizza().getPrize()!=null
			&& orderRequest.getPizza().getPrize().compareTo(new BigDecimal(0))<=0) {
			throw new UiException("Amount value must be positive");
		} else if (orderRequest !=null && orderRequest.getDelivery()!=null
			&& orderRequest.getDelivery().getTo() != null
			&& orderRequest.getDelivery().getTo().getAddress().isEmpty()) {
			throw new UiException("Delivery Address can not be empty");
		} else if (orderRequest !=null && orderRequest.getPerson()!=null
			&& orderRequest.getPerson().getEmail().isEmpty()) {
			throw new UiException("Person email can not be empty");
		}

//		OrderResponse orderResponse = new OrderResponse();
//		orderResponse.setOrderId("200");
//		orderResponse.setOrderStatus("Pizza ordered correctly");
//		orderResponse.setOrderDescription("Pizza ordered");

		try {
			String orderRequestJson = objectMapper.writeValueAsString(orderRequest);
			String responseJson = producerTemplate.requestBody("http://gateway:8090/api/microOrdering/order", orderRequestJson, String.class);
			return objectMapper.readValue(responseJson, OrderResponse.class);
		} catch (Exception e) {
			System.err.println("Error occurred while processing the order: " + e.getMessage());
			e.printStackTrace(); // Add this line to print the stack trace
			throw new UiException("Error occurred while processing the order: " + e.getMessage());
		}
	}

	@ExceptionHandler(UiException.class)
	public ExceptionResponse handleUiException(UiException ex) {
		// Log the exception
		System.err.println("Handling UI exception: " + ex.getMessage());
		return new ExceptionResponse(Date.from(Instant.now()), ex.getLocalizedMessage(), "Error occurred while processing the order: " + ex.getMessage());
	}
}
