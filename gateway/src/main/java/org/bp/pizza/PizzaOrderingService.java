package org.bp.pizza;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.bp.pizza.model.PizzaInfo;
import org.bp.pizza.model.PizzaOrderRequest;
import org.bp.pizza.model.Utils;
import org.bp.pizza.model.ExceptionResponse;
import org.bp.pizza.state.ProcessingEvent;
import org.bp.pizza.state.ProcessingState;
import org.bp.pizza.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.bp.pizza.exceptions.DeliveryException;
import org.bp.pizza.exceptions.PizzaException;

import java.time.OffsetDateTime;

import static org.apache.camel.model.rest.RestParamType.body;

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
		if (travelServiceType.equals("all") || travelServiceType.equals("pizza"))
			pizzaCreationExceptionHandlers();
		if (travelServiceType.equals("all") || travelServiceType.equals("delivery"))
			deliveryExceptionHandlers();
		if (travelServiceType.equals("all") || travelServiceType.equals("gateway"))
			gateway();
		if (travelServiceType.equals("all") || travelServiceType.equals("pizza"))
			PizzaCreation();
		if (travelServiceType.equals("all") || travelServiceType.equals("delivery"))
			delivery();
		if (travelServiceType.equals("all") || travelServiceType.equals("payment"))
			payment();
//		setCompleted();
    }

    private void gateway() {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .contextPath("/api")
                // turn on swagger api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Micro Pizza ordering API")
                .apiProperty("api.version", "1.0.0");

        rest("/pizza").description("Micro Pizza ordering REST service")
                .consumes("application/json")
                .produces("application/json")
                .post("/order").description("Order a pizza").type(PizzaOrderRequest.class).outType(PizzaInfo.class)
                .param().name("body").type(body).description("The pizza to order").endParam()
                .responseMessage().code(200).message("Pizza successfully ordered").endResponseMessage()
                .to("direct:orderPizza");
//				.get("/order/status/{orderId}").description("Get coffee status").outType(CoffeeOrder.class)
//				.param().name("orderId").type(RestParamType.path).description("Order id").endParam()
//				.responseMessage().code(200).message("Coffee status").endResponseMessage()
//				.responseMessage().code(404).message("Order not found").endResponseMessage()
//				.to("direct:get-coffee-status");

        from("direct:orderPizza").routeId("orderPizza")
                .log("orderPizza fired")
                .process(
                        (exchange) -> {
							PizzaOrderRequest request = exchange.getMessage().getBody(PizzaOrderRequest.class);
                    exchange.getMessage().setHeader("pizzaCreationId", pizzaIdentifierService.getPizzaIdentifier());
                })
                .to("direct:OrderPizzaRequest")
                .to("direct:pizzaRequester");

        from("direct:pizzaRequester").routeId("pizzaRequester")
                .log("pizzaRequester fired")
                .process(
                        (exchange) -> {
                            exchange.getMessage().setBody(Utils.preparePizzaInfo(
                                    exchange.getMessage().getHeader("pizzaCreationId", String.class), null));
                        }
                );

        from("direct:OrderPizzaRequest").routeId("OrderPizzaRequest")
                .log("brokerTopic fired")
                .marshal().json()
				.to("kafka:PizzaReqTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType);
    }

	private void pizzaCreationExceptionHandlers() {
		onException(PizzaException.class)
		.process((exchange) -> {
					ExceptionResponse er = new ExceptionResponse();
					er.setTimestamp(OffsetDateTime.now());
					Exception cause = exchange.getProperty(exchange.EXCEPTION_CAUGHT, Exception.class);
					er.setMessage(cause.getMessage());
					exchange.getMessage().setBody(er);
				})
        .marshal().json()
		.to("stream:out")
		.setHeader("serviceType", constant("pizza"))
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
					exchange.getMessage().setBody(er);
				})
	    .marshal().json()
		.to("stream:out")
		.setHeader("serviceType", constant("delivery"))
				.to("kafka:PizzaOrderingFailTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType)
		.handled(true);
	}

	private void PizzaCreation() {
		from("kafka:PizzaReqTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("createPizza")
		.log("fired createPizza")
		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
		.process(
				(exchange) -> {
					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
					ProcessingState previousState = pizzaStateService.sendEvent(pizzaCreationId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {
						PizzaInfo pi = new PizzaInfo();
						pi.setId(pizzaIdentifierService.getPizzaIdentifier());
						PizzaOrderRequest por = exchange.getMessage().getBody(PizzaOrderRequest.class);
						if (por != null && por.getPizza() != null) {
							float prize = 40.0f;
							String ingredients = por.getPizza().getIngredients();
							if (ingredients.contains("ananas")) {
								throw new PizzaException("No ingredient: " + ingredients);
							} else if (ingredients.length() >= 10) {
								pi.setCost(prize + (por.getPizza().getIngredients().length() * 1.5f));
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
			.setHeader("serviceType", constant("pizza"))
				.to("kafka:PizzaInfoTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType)
		.endChoice();

		from("kafka:PizzaOrderingFailTopic?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("pizzaCreationCompensation")
		.log("fired pizzaCreationCompensation")
		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
		.choice()
			.when(header("serviceType").isNotEqualTo("pizza"))
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
		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
		.process(
				(exchange) -> {
					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
					ProcessingState previousState = deliveryStateService.sendEvent(pizzaCreationId, ProcessingEvent.START);
					if (previousState != ProcessingState.CANCELLED) {
						PizzaInfo pi = new PizzaInfo();
						pi.setId(pizzaIdentifierService.getPizzaIdentifier());

						PizzaOrderRequest por = exchange.getMessage().getBody(PizzaOrderRequest.class);
						if (por != null && por.getDelivery() != null &&
								por.getDelivery().getFrom() != null && por.getDelivery().getFrom().getAddress() != null) {
							String place = por.getDelivery().getFrom().getAddress();
							if (place.equals("Poznan")) {
								pi.setCost(10f);
							} else if (place.equals("Warsaw")) {
								throw new DeliveryException("Not serviced destination: " + place);
							} else {
								pi.setCost(20f);
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
				.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
				.process(
						(exchange) -> {
							String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
							boolean isReady = paymentService.addPizzaOrderRequest(
									pizzaCreationId,
									exchange.getMessage().getBody(PizzaOrderRequest.class));
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
							Float pizzaCost = paymentData.pizzaPizzaInfo.getCost();
							Float deliveryCost = paymentData.deliveryPizzaInfo.getCost();
							Float totalCost = pizzaCost + deliveryCost;
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

//	private void setCompleted() {
//		from("kafka:finalize?brokers=" + travelKafkaServer + "&groupId=" + travelServiceType).routeId("finalize")
//			.log("fired finalize")
////			.to("stream:out")
//				.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
//				.process(
//						(exchange) -> {
//							String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
//							ProcessingState previousState = deliveryStateService.sendEvent(pizzaCreationId, ProcessingEvent.COMPLETE);
////							if (previousState != ProcessingState.CANCELLED || previousState != ProcessingState.FINISHED) {
////								PizzaInfo pi = new PizzaInfo();
////								pi.setId(pizzaIdentifierService.getPizzaIdentifier());
////
////								PizzaOrderRequest por = exchange.getMessage().getBody(PizzaOrderRequest.class);
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
