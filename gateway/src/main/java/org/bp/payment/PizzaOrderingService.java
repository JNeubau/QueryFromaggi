package org.bp.payment;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.bp.payment.model.*;
import org.bp.payment.model.order.OrderRequest;
import org.bp.payment.model.order.OrderResponse;
import org.bp.payment.model.payment.PaymentRequest;
import org.bp.payment.model.payment.PaymentResponse;
import org.bp.payment.state.ProcessingEvent;
import org.bp.payment.state.ProcessingState;
import org.bp.payment.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.bp.payment.exceptions.DeliveryException;
import org.bp.payment.exceptions.PizzaException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Component
public class PizzaOrderingService extends RouteBuilder {

    @Autowired
    PizzaIdentifierService pizzaIdentifierService;

	@Autowired
	PaymentService paymentService;

	@Autowired
	StateService deliveryStateService;

	@Autowired
	StateService pizzaStateService;

	@org.springframework.beans.factory.annotation.Value("${travel.kafka.server}")
	private String travelKafkaServer;

	@org.springframework.beans.factory.annotation.Value("${travel.service.type}")
	private String travelServiceType;

    @Override
    public void configure() throws Exception {
		onException(Exception.class)
            .process(exchange -> {
                Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                ExceptionResponse er = new ExceptionResponse();
                er.setTimestamp(OffsetDateTime.now());
                er.setMessage(cause.getMessage());
                cause.printStackTrace(); // Add this line to print the stack trace
                exchange.getMessage().setBody(er);
            })
            .marshal().json(JsonLibrary.Jackson)
            .to("stream:out")
            .handled(true);
			
//		restConfiguration()
//			.component("servlet")
//			.bindingMode(RestBindingMode.json)
//			.dataFormatProperty("prettyPrint", "true")
//			.enableCORS(true)
//			.contextPath("/api")
//			.apiContextPath("/api-doc")
//			.apiProperty("api.title", "Travel booking API")
//			.apiProperty("api.version", "1.0.0");
//
//		rest("/order").description("Pizza REST service")
//				.consumes("application/json")
//				.produces("application/json")
//				.post("/order").description("Order a pizza")
//				.type(TravelBookingRequest.class).outType(org.bp.payment.PaymentResponse.class)
//					.param().name("body").type(body).description("The travel to book").endParam()
//					.responseMessage().code(200).message("Travel successfully booked").endResponseMessage()
//					.to("direct:bookTravel");

		if (travelServiceType.equals("all") || travelServiceType.equals("order"))
			pizzaCreationExceptionHandlers();
		if (travelServiceType.equals("all") || travelServiceType.equals("delivery"))
			deliveryExceptionHandlers();
		if (travelServiceType.equals("all") || travelServiceType.equals("gateway"))
			gateway();
		if (travelServiceType.equals("all") || travelServiceType.equals("order"))
			PizzaCreation();
		if (travelServiceType.equals("all") || travelServiceType.equals("delivery"))
			delivery();
		if (travelServiceType.equals("all") || travelServiceType.equals("payment"))
			payment();
//		final JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(org.bp.payment.)
//			PaymentSOAP();
    }

    private void gateway() {
		restConfiguration()
				.component("servlet")
				.bindingMode(RestBindingMode.json)
				.dataFormatProperty("prettyPrint", "true")
				.enableCORS(true)
				.contextPath("/api")
				.host("localhost")
				.port(8090)
				.apiContextPath("/api-doc")
				.apiProperty("api.title", "Pizza ordering API")
				.apiProperty("api.version", "1.0.0");

        rest("/microOrdering").description("Pizza ordering REST service")
                .consumes("application/json")
                .produces("application/json")
                .post("/order").description("Handle Order")
				.type(OrderRequest.class)
				.outType(OrderResponse.class)
                .param().name("body").type(RestParamType.body).description("The order to order").endParam()
                .responseMessage().code(200).message("Pizza successfully ordered").endResponseMessage()
				.responseMessage().code(400).message("Bad Request").endResponseMessage()
                .to("direct:orderPizza2")
				.post("/payment").description("Handle Payment")
				.type(OrderRequest.class)
				.outType(OrderResponse.class)
				.param().name("body").type(RestParamType.body).description("The order to order").endParam()
				.responseMessage().code(200).message("Pizza successfully ordered").endResponseMessage()
				.responseMessage().code(400).message("Bad Request").endResponseMessage()
				.to("direct:payment2");

		from("direct:orderPizza2").routeId("orderPizza")
				.log("OrderPizza2 fired")
				.process(exchange -> {
					try {
						OrderRequest request = exchange.getMessage().getBody(OrderRequest.class);
						OrderResponse response = Utils.createOrderResponse();
						exchange.getMessage().setBody(response);
					} catch (CamelExecutionException e) {
						System.err.println("Exception occurred during camel on the exchange: " + e.getMessage());
						throw e;
					} catch (Exception e) {
						System.err.println("Exception occurred during execution on the exchange: " + e.getMessage());
						throw e;
					}
				})
				.log("Processed order response: ${body}");

		from("direct:payment2").routeId("payment")
				.log("payment fired")
				.process(exchange -> {
					try {
						PaymentRequest request = exchange.getMessage().getBody(PaymentRequest.class);
						PaymentResponse response = Utils.createPaymentResponse();
						exchange.getMessage().setBody(response);
					} catch (CamelExecutionException e) {
						System.err.println("Exception occurred during camel on the exchange: " + e.getMessage());
						throw e;
					} catch (Exception e) {
						System.err.println("Exception occurred during execution on the exchange: " + e.getMessage());
						throw e;
					}
				})
				.log("Processed payment response: ${body}");
    }

	private void pizzaCreationExceptionHandlers() {
		onException(PizzaException.class)
		.process((exchange) -> {
					ExceptionResponse er = new ExceptionResponse();
					er.setTimestamp(OffsetDateTime.now());
					Exception cause = exchange.getProperty(exchange.EXCEPTION_CAUGHT, Exception.class);
					er.setMessage(cause.getMessage());
					cause.printStackTrace(); // Add this line to print the stack trace
					exchange.getMessage().setBody(er);
				})
        .marshal().json()
		.to("stream:out")
		.setHeader("serviceType", constant("order"))
				.to("kafka:PizzaOrderingFailTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType)
		.handled(true);
    }

	private void deliveryExceptionHandlers() {
		onException(DeliveryException.class)
		.process((exchange) -> {
					ExceptionResponse er = new ExceptionResponse();
					er.setTimestamp(OffsetDateTime.now());
					Exception cause = exchange.getProperty(exchange.EXCEPTION_CAUGHT, Exception.class);
					er.setMessage(cause.getMessage());
					cause.printStackTrace(); // Add this line to print the stack trace
					exchange.getMessage().setBody(er);
				})
	    .marshal().json()
		.to("stream:out")
		.setHeader("serviceType", constant("delivery"))
				.to("kafka:PizzaOrderingFailTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType)
		.handled(true);
	}

	private void PizzaCreation() {
//		from("kafka:orderPizza2?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("orderPizza")
//				.log("orderPizza fired")
//				.process(exchange -> {
//					OrderRequest request = exchange.getMessage().getBody(OrderRequest.class);
//					// Process the request and create an OrderResponse
//					OrderResponse response = new OrderResponse();
//					response.setOrderId(pizzaIdentifierService.getPizzaIdentifier());
//					response.setOrderStatus("SUCCESS");
//					response.setOrderDescription("Pizza order processed successfully");
//					exchange.getMessage().setBody(response);
//				});

		from("kafka:PizzaReqTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("createPizza")
		.log("fired createPizza")
		.unmarshal().json(JsonLibrary.Jackson, OrderRequest.class)
		.process(
				(exchange) -> {
					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
					ProcessingState previousState = pizzaStateService.sendEvent(pizzaCreationId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {
						PizzaInfo pi = new PizzaInfo();
						pi.setId(pizzaIdentifierService.getPizzaIdentifier());
						OrderRequest por = exchange.getMessage().getBody(OrderRequest.class);
						if (por != null && por.getPizza() != null) {
							BigDecimal prize = new BigDecimal(40);
							String ingredients = por.getPizza().getIngredients();
							if (ingredients.contains("ananas")) {
								throw new PizzaException("No ingredient: " + ingredients);
							} else if (ingredients.length() >= 10) {
								pi.setCost(prize.add(BigDecimal.valueOf(por.getPizza().getIngredients().length() * 2L)));
//								pi.setCost(prize + (por.getPizza().getIngredients().length() * 1.5f));
							} else {
								pi.setCost(prize);
							}
						}
						exchange.getMessage().setBody(pi);
						previousState = pizzaStateService.sendEvent(pizzaCreationId, ProcessingEvent.FINISH);
					}
					exchange.getMessage().setHeader("previousState", previousState);
                })
        .marshal().json()
        .to("stream:out")
		.choice()
			.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
			.to("direct:pizzaCreationCompensationAction")
		.otherwise()
			.setHeader("serviceType", constant("order"))
				.to("kafka:PizzaInfoTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType)
		.endChoice();

		from("kafka:PizzaOrderingFailTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("pizzaCreationCompensation")
		.log("fired pizzaCreationCompensation")
		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
		.choice()
			.when(header("serviceType").isNotEqualTo("order"))
		    .process((exchange) -> {
				String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
				ProcessingState previousState = pizzaStateService.sendEvent(pizzaCreationId, ProcessingEvent.CANCEL);
				exchange.getMessage().setHeader("previousState", previousState);
		    })
		    .choice()
		    	.when(header("previousState").isEqualTo(ProcessingState.FINISHED))
				.to("direct:pizzaCreationCompensationAction")
			.endChoice()
		 .endChoice();

		from("direct:pizzaCreationCompensationAction").routeId("pizzaCreationCompensationAction")
		.log("fired pizzaCreationCompensationAction")
		.to("stream:out");
//		.to("kafka:finalize?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType);
	}

	private void delivery() {
		from("kafka:PizzaReqTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("makeDelivery")
		.log("fired makeDelivery")
		.unmarshal().json(JsonLibrary.Jackson, OrderRequest.class)
		.process(
				(exchange) -> {
					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
					ProcessingState previousState = deliveryStateService.sendEvent(pizzaCreationId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {
						PizzaInfo pi = new PizzaInfo();
						pi.setId(pizzaIdentifierService.getPizzaIdentifier());

						OrderRequest por = exchange.getMessage().getBody(OrderRequest.class);
						if (por != null && por.getDelivery() != null &&
								por.getDelivery().getFrom() != null && por.getDelivery().getFrom().getAddress() != null) {
							String place = por.getDelivery().getFrom().getAddress();
							if (place.equals("Poznan")) {
								pi.setCost(BigDecimal.valueOf(10));
							} else if (place.equals("Warsaw")) {
								throw new DeliveryException("Not serviced destination: " + place);
							} else {
								pi.setCost(BigDecimal.valueOf(20));
							}
						}
						exchange.getMessage().setBody(pi);
						previousState = deliveryStateService.sendEvent(pizzaCreationId, ProcessingEvent.FINISH);
					}
					exchange.getMessage().setHeader("previousState", previousState);
				})
		.marshal().json()
		.to("stream:out")
		.choice()
			.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
			.to("direct:makeDeliveryCompensationAction")
		.otherwise()
			.setHeader("serviceType", constant("delivery"))
				.to("kafka:PizzaInfoTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType)
		.endChoice();

		from("kafka:PizzaOrderingFailTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("makeDeliveryCompensation")
		.log("fired makeDeliveryCompensation")
		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
        .choice()
    		.when(header("serviceType").isNotEqualTo("delivery"))
            .process((exchange) -> {
    			String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
    			ProcessingState previousState = deliveryStateService.sendEvent(pizzaCreationId, ProcessingEvent.CANCEL);
    			exchange.getMessage().setHeader("previousState", previousState);
            })
            .choice()
            	.when(header("previousState").isEqualTo(ProcessingState.FINISHED))
    			.to("direct:makeDeliveryCompensationAction")
    		.endChoice()
         .endChoice();

		from("direct:makeDeliveryCompensationAction").routeId("makeDeliveryCompensationAction")
		.log("fired makeDeliveryCompensationAction")
		.to("stream:out")
				.to("kafka:finalize?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType);
	}

	private void payment() {
		from("kafka:PizzaInfoTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("paymentPizzaInfo")
				.log("fired paymentPizzaInfo")
				.unmarshal().json(JsonLibrary.Jackson, PizzaInfo.class)
				.process(
						(exchange) -> {
							String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
							boolean isReady = paymentService.addPizzaInfo(
									pizzaCreationId,
									exchange.getMessage().getBody(PizzaInfo.class),
									exchange.getMessage().getHeader("serviceType", String.class));
							exchange.getMessage().setHeader("isReady", isReady);
						})
				.choice()
				.when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
				.endChoice();

		from("kafka:PizzaReqTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("paymentPizzaReq")
				.log("fired paymentPizzaReq")
				.unmarshal().json(JsonLibrary.Jackson, OrderRequest.class)
				.process(
						(exchange) -> {
							String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
							boolean isReady = paymentService.addPizzaOrderRequest(
									pizzaCreationId,
									exchange.getMessage().getBody(OrderRequest.class));
							exchange.getMessage().setHeader("isReady", isReady);
						})
				.choice()
				.when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
				.endChoice();

		from("direct:finalizePayment").routeId("finalizePayment")
				.log("fired finalizePayment")
				.process(
						(exchange) -> {
							String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
							PaymentService.PaymentData paymentData = paymentService.getPaymentData(pizzaCreationId);
							BigDecimal pizzaCost = paymentData.pizzaPizzaInfo.getCost();
							BigDecimal deliveryCost = paymentData.deliveryPizzaInfo.getCost();
							BigDecimal totalCost = pizzaCost.add(deliveryCost);
							PizzaInfo pizzaInfo = new PizzaInfo();
							pizzaInfo.setId(pizzaCreationId);
							pizzaInfo.setCost(totalCost);
							exchange.getMessage().setHeader("pizzaCreationId", pizzaIdentifierService.getPizzaIdentifier());
							exchange.getMessage().setBody(pizzaInfo);
						})
				.to("direct:notification");

			from("direct:notification").routeId("notification")
			.log("fired notification")
			.to("stream:out");
//			.to("kafka:finalize?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType);
	}

	//	private void PaymentSOAP() {
//		from("kafka:PizzaReqTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("createSOAPPizza")
//				.log("fired createSOAPPizza")
//				.unmarshal().json(JsonLibrary.Jackson, OrderRequest.class)
//				.process(
//						(exchange) -> {
//							String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
//							ProcessingState previousState = pizzaStateService.sendEvent(pizzaCreationId, ProcessingEvent.START);
//						})
//		.to("direct:OrderPizzaRequest")
//		.to("direct:pizzaRequester");
//
//		from("direct:pizzaRequester").routeId("pizzaRequester")
//		.log("pizzaRequester fired")
//		.process(
//				(exchange) -> {
//					exchange.getMessage().setBody(Utils.preparePizzaInfo(
//							exchange.getMessage().getHeader("pizzaCreationId", String.class), null));
//				}
//		);
////		http://localhost:8080/soap-api/service/payment
//
//		from("direct:OrderPizzaRequest").routeId("OrderPizzaRequest")
//		.log("brokerTopic fired")
//		.marshal().json()
//		.to("kafka:PizzaReqTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType);
//	}

//	private void setCompleted() {
//		from("kafka:finalize?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("finalize")
//			.log("fired finalize")
////			.to("stream:out")
//				.unmarshal().json(JsonLibrary.Jackson, OrderRequest.class)
//				.process(
//						(exchange) -> {
//							String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
//							ProcessingState previousState = deliveryStateService.sendEvent(pizzaCreationId, ProcessingEvent.COMPLETE);
////							if (previousState != ProcessingState.CANCELLED || previousState != ProcessingState.FINISHED) {
////								PizzaInfo pi = new PizzaInfo();
////								pi.setId(pizzaIdentifierService.getPizzaIdentifier());
////
////								OrderRequest por = exchange.getMessage().getBody(OrderRequest.class);
////								if (por != null && por.getDelivery() != null &&
////										por.getDelivery().getFrom() != null && por.getDelivery().getFrom().getAddress() != null) {
////									String place = por.getDelivery().getFrom().getAddress();
////									if (place.equals("Poznan")) {
////										pi.setCost(10f);
////									} else if (place.equals("Warsaw")) {
////										throw new DeliveryException("Not serviced destination: " + place);
////									} else {
////										pi.setCost(20f);
////									}
////								}
////								exchange.getMessage().setBody(pi);
////								previousState = deliveryStateService.sendEvent(pizzaCreationId, ProcessingEvent.FINISH);
////							}
//							exchange.getMessage().setHeader("Completed from", previousState);
//						})
//				.marshal().json()
//				.to("stream:out");
////				.choice()
////				.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
////				.to("direct:makeDeliveryCompensationAction")
////				.otherwise()
////				.setHeader("serviceType", constant("delivery"))
////				.to("kafka:PizzaInfoTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType)
////				.endChoice();
//    }

}
