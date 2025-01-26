package org.bp.ui;
import java.math.BigDecimal;

import org.apache.camel.ProducerTemplate;
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
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController

@OpenAPIDefinition(info = @Info(
        title = "Payment service",
        version = "1",
        description = "Service for payment"))

public class PaymentService {

		@Autowired
		private ProducerTemplate producerTemplate;

		@PostMapping("/payment")
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
		public PaymentResponse payment(
				@org.springframework.web.bind.annotation.RequestBody PaymentRequest paymentRequest) {
			if (paymentRequest !=null && paymentRequest.getAmount()!=null
					&& paymentRequest.getAmount().getValue()!=null
					&& paymentRequest.getAmount().getValue().compareTo(new BigDecimal(0))<=0) {

				throw new PaymentException("Amount value must be positive");

			}

			try {
				return producerTemplate.requestBody("direct:microBooking", paymentRequest, PaymentResponse.class);
			} catch (Exception e) {
				throw new PaymentException("Error occurred while processing the order: " + e.getMessage());
			}

//			PaymentResponse paymentResponse = new PaymentResponse();
//			paymentResponse.setTransactionDate(new Date());
//			paymentResponse.setTransactionId(200);
//			return paymentResponse;
		}


}
