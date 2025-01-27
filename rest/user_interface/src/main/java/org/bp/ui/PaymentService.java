package org.bp.ui;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.Model;
import org.bp.ui.model.order.OrderResponse;
import org.bp.ui.model.order.UiException;
import org.bp.ui.model.payment.PaymentException;
import org.bp.ui.model.payment.PaymentRequest;
import org.bp.ui.model.payment.PaymentResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController

@OpenAPIDefinition(info = @Info(
        title = "Payment service",
        version = "1",
        description = "Service for payment"))

public class PaymentService {

		@Autowired
		private ProducerTemplate producerTemplate;

		@Autowired
		private org.springframework.web.client.RestTemplate restTemplate;

		@Autowired
		private ObjectMapper objectMapper;

//		@PostMapping("/payment")
//	    @Operation(
//	            summary = "payment operation",
//	            description = "operation for payment",
//	            responses = {
//	                @ApiResponse(responseCode = "200",
//	                        description = "OK",
//	                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))}),
//	                @ApiResponse(responseCode = "400", description = "Bad Request",
//	                        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
//	            })
//		public PaymentResponse payment(
//				@org.springframework.web.bind.annotation.RequestBody PaymentRequest paymentRequest) {
//			if (paymentRequest !=null && paymentRequest.getAmount()!=null
//					&& paymentRequest.getAmount().getValue()!=null
//					&& paymentRequest.getAmount().getValue().compareTo(new BigDecimal(0))<=0) {
//				throw new PaymentException("Amount value must be positive");
//			}
//
//			try {
//				String paymentRequestJson = objectMapper.writeValueAsString(paymentRequest);
//				String responseJson = producerTemplate.requestBody("http://gateway:8090/api/microOrdering/payment", paymentRequestJson, String.class);
//				return objectMapper.readValue(responseJson, PaymentResponse.class);
//			} catch (Exception e) {
//				System.err.println("Error occurred while processing the payment: " + e.getMessage());
//				e.printStackTrace(); // Add this line to print the stack trace
//				throw new PaymentException("Error occurred while processing the order: " + e.getMessage());
//			}
//		}

	@GetMapping("/payment/{orderId}")
	@Operation(
			summary = "payment operation",
			description = "operation for payment",
			responses = {
					@ApiResponse(responseCode = "200",
							description = "OK",
							content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))}),
					@ApiResponse(responseCode = "400", description = "Bad Request",
							content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
			})
	public PaymentResponse status(@PathVariable String orderId) {
		try {
			System.out.println("Order ID: " + orderId);
			String paymentRequestJson = objectMapper.writeValueAsString(orderId);
			ResponseEntity<PaymentResponse> response = restTemplate.getForEntity(
					"http://gateway:8090/api/microOrdering/payment/" + orderId.toString(),
					PaymentResponse.class);
			return response.getBody();
		} catch (Exception e) {
			System.err.println("Error occurred while processing the payment: " + e.getMessage());
			e.printStackTrace();
			throw new PaymentException("Error occurred while processing the order: " + e.getMessage());
		}
	}

}
