package org.bp.ui;
import java.math.BigDecimal;
import java.util.Date;

import org.bp.ui.model.payment.PaymentException;
import org.bp.ui.model.payment.PaymentRequest;
import org.bp.ui.model.payment.PaymentResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController

@OpenAPIDefinition(info = @Info(
        title = "Payment service",
        version = "1",
        description = "Service for payment"))

public class PaymentService {
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

			PaymentResponse paymentResponse = new PaymentResponse();
			paymentResponse.setTransactionDate(new Date());
			paymentResponse.setTransactionId(200);
			return paymentResponse;
		}


}
