package org.bp.pizza;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.bp.pizza.model.PizzaInfo;
import org.bp.pizza.model.PizzaOrderRequest;
import org.bp.pizza.model.Utils;
import org.bp.pizza.state.ProcessingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.apache.camel.model.rest.RestParamType.body;

@Component
public class PizzaOrderingService extends RouteBuilder {

    @Autowired
    PizzaIdentifierService pizzaIdentifierService;

	@Autowired
	PaymentService paymentService;

//	@Autowired
//	StateService flightStateService;
//
//	@Autowired
//	StateService hotelStateService;

    @Override
    public void configure() throws Exception {
//		bookHotelExceptionHandlers();
//		bookFlightExceptionHandlers();
        gateway();
        PizzaCreation();
		delivery();
		payment();
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

        from("direct:orderPizza").routeId("orderPizza")
                .log("orderPizza fired")
                .process(
                        (exchange) -> {
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
                .to("kafka:PizzaReqTopic?brokers=localhost:9092");
    }

//	private void bookHotelExceptionHandlers() {
//		onException(HotelException.class)
//		.process((exchange) -> {
//					ExceptionResponse er = new ExceptionResponse();
//					er.setTimestamp(OffsetDateTime.now());
//					Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
//					er.setMessage(cause.getMessage());
//					exchange.getMessage().setBody(er);
//				}
//				)
//        .marshal().json()
//		.to("stream:out")
//		.setHeader("serviceType", constant("hotel"))
//		.to("kafka:TravelBookingFailTopic?brokers=localhost:9092")
//		.handled(true)
//		;
//    }

//	private void bookFlightExceptionHandlers() {
//		onException(FlightException.class)
//		.process((exchange) -> {
//					ExceptionResponse er = new ExceptionResponse();
//					er.setTimestamp(OffsetDateTime.now());
//					Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
//					er.setMessage(cause.getMessage());
//					exchange.getMessage().setBody(er);
//				}
//				)
//	    .marshal().json()
//		.to("stream:out")
//		.setHeader("serviceType", constant("flight"))
//		.to("kafka:TravelBookingFailTopic?brokers=localhost:9092")
//		.handled(true)
//		;
//	}

	private void PizzaCreation() {
		from("kafka:PizzaReqTopic?brokers=localhost:9092").routeId("createPizza")
		.log("fired createPizza")
		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
		.process(
				(exchange) -> {
                    PizzaInfo pi = new PizzaInfo();
                    pi.setId(pizzaIdentifierService.getPizzaIdentifier());
                    PizzaOrderRequest por = exchange.getMessage().getBody(PizzaOrderRequest.class);
                    if (por != null && por.getPizza() != null) {
                        float prize = 40.0f;
                        if (por.getPizza().getIngredients() != null) {
                            pi.setCost(prize + (por.getPizza().getIngredients().length() * 1.5f));
                        }
                            pi.setCost(prize);
                    }
                    exchange.getMessage().setBody(pi);
                })
        .marshal().json()
        .to("stream:out")
        .setHeader("serviceType", constant("pizza"))
        .to("kafka:PizzaInfoTopic?brokers=localhost:9092");

//					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
//					ProcessingState previousState = hotelStateService.sendEvent(pizzaCreationId, ProcessingEvent.START);
//					if (previousState!=ProcessingState.CANCELLED) {
//		    			PizzaInfo bi = new PizzaInfo();
//		    			bi.setId(bookingIdentifierService.getBookingIdentifier());
//
//		    			PizzaOrderRequest btr= exchange.getMessage().getBody(PizzaOrderRequest.class);
//		    			if (btr!=null && btr.getHotel()!=null
//		    					&& btr.getHotel().getCountry()!=null ) {
//		    				String country = btr.getHotel().getCountry();
//		    				if (country.equals("USA")) {
//		    					bi.setCost(new BigDecimal(999));
//		    				}
//		    				else if (country.equals("China")){
//		    					throw new HotelException("Not serviced destination: "+country);
//		    				}
//		    				else {
//		    					bi.setCost(new BigDecimal(888));
//		    				}
//		    			}
//		    			exchange.getMessage().setBody(bi);
//		    			previousState = hotelStateService.sendEvent(pizzaCreationId, ProcessingEvent.FINISH);
//		    			}
//					exchange.getMessage().setHeader("previousState", previousState);        		}
//				)

//		from("kafka:TravelBookingFailTopic?brokers=localhost:9092").routeId("bookHotelCompensation")
//		.log("fired bookHotelCompensation")
//		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
//		.choice()
//			.when(header("serviceType").isNotEqualTo("hotel"))
//		    .process((exchange) -> {
//				String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
//				ProcessingState previousState = hotelStateService.sendEvent(pizzaCreationId, ProcessingEvent.CANCEL);
//				exchange.getMessage().setHeader("previousState", previousState);
//		    })
//		    .choice()
//		    	.when(header("previousState").isEqualTo(ProcessingState.FINISHED))
//				.to("direct:bookHotelCompensationAction")
//			.endChoice()
//		 .endChoice();
//
//		from("direct:bookHotelCompensationAction").routeId("bookHotelCompensationAction")
//		.log("fired bookHotelCompensationAction")
//		.to("stream:out");
	}

	private void delivery() {
		from("kafka:PizzaReqTopic?brokers=localhost:9092").routeId("makeDelivery")
		.log("fired makeDelivery")
		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
		.process(
				(exchange) -> {
                    PizzaInfo pi = new PizzaInfo();
                    pi.setId(pizzaIdentifierService.getPizzaIdentifier());

                    PizzaOrderRequest por = exchange.getMessage().getBody(PizzaOrderRequest.class);
                    if (por != null && por.getDelivery() != null &&
                            por.getDelivery().getFrom() != null && por.getDelivery().getFrom().getAddress() != null) {
                        String place = por.getDelivery().getFrom().getAddress();
                        if (place.equals("Poznan")) {
                            pi.setCost(10f);
                        } else {
                            pi.setCost(20f);
                        }
                    }
                    exchange.getMessage().setBody(pi);
//					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
//					ProcessingState previousState = flightStateService.sendEvent(pizzaCreationId, ProcessingEvent.START);
//					if (previousState!=ProcessingState.CANCELLED) {
//		    			PizzaInfo bi = new PizzaInfo();
//		    			bi.setId(bookingIdentifierService.getBookingIdentifier());
//		    			PizzaOrderRequest btr= exchange.getMessage().getBody(PizzaOrderRequest.class);
//		    			if (btr!=null && btr.getFlight()!=null && btr.getFlight().getFrom()!=null
//		    					&& btr.getFlight().getFrom().getAirport()!=null) {
//		    				String from=btr.getFlight().getFrom().getAirport();
//		    				if (from.equals("PEK")){
//		    					throw new FlightException("Not serviced airport: "+from);
//		    				}
//		    				else if (from.equals("JFK")) {
//		    					bi.setCost(new BigDecimal(700));
//		    				}
//		    				else {
//		    					bi.setCost(new BigDecimal(600));
//		    				}
//		    			}
//
//		    			exchange.getMessage().setBody(bi);
//		    			previousState = flightStateService.sendEvent(pizzaCreationId, ProcessingEvent.FINISH);
//		    			}
//					exchange.getMessage().setHeader("previousState", previousState);
				}
				)
		.marshal().json()
		.to("stream:out")
        .setHeader("serviceType", constant("delivery"))
        .to("kafka:PizzaInfoTopic?brokers=localhost:9092");
//		.choice()
//			.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
//			.to("direct:makeDeliveryCompensationAction")
//		.otherwise()
//			.setHeader("serviceType", constant("flight"))
//			.to("kafka:BookingInfoTopic?brokers=localhost:9092")
//		.endChoice()
//		;

//		from("kafka:TravelBookingFailTopic?brokers=localhost:9092").routeId("makeDeliveryCompensation")
//		.log("fired makeDeliveryCompensation")
//		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
//        .choice()
//    		.when(header("serviceType").isNotEqualTo("flight"))
//            .process((exchange) -> {
//    			String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
//    			ProcessingState previousState = flightStateService.sendEvent(pizzaCreationId, ProcessingEvent.CANCEL);
//    			exchange.getMessage().setHeader("previousState", previousState);
//            })
//            .choice()
//            	.when(header("previousState").isEqualTo(ProcessingState.FINISHED))
//    			.to("direct:makeDeliveryCompensationAction")
//    		.endChoice()
//         .endChoice();
//
//		from("direct:makeDeliveryCompensationAction").routeId("makeDeliveryCompensationAction")
//		.log("fired makeDeliveryCompensationAction")
//		.to("stream:out");
	}

	private void payment() {
		from("kafka:PizzaInfoTopic?brokers=localhost:9092").routeId("paymentPizzaInfo")
		.log("fired paymentPizzaInfo")
		.unmarshal().json(JsonLibrary.Jackson, PizzaInfo.class)
		.process(
				(exchange) -> {
					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
					boolean isReady= paymentService.addPizzaInfo(
							pizzaCreationId,
							exchange.getMessage().getBody(PizzaInfo.class),
							exchange.getMessage().getHeader("serviceType", String.class));
					exchange.getMessage().setHeader("isReady", isReady);
				})
		.choice()
        .when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
        .endChoice();

		from("kafka:PizzaReqTopic?brokers=localhost:9092").routeId("paymentPizzaReq")
		.log("fired paymentPizzaReq")
		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
		.process(
				(exchange) -> {
					String pizzaCreationId = exchange.getMessage().getHeader("pizzaCreationId", String.class);
					 boolean isReady= paymentService.addPizzaOrderRequest(
							pizzaCreationId,
							exchange.getMessage().getBody(PizzaOrderRequest.class));
					 exchange.getMessage().setHeader("isReady", isReady);
				}
				)
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
				 exchange.getMessage().setBody(pizzaInfo);
			})
	.to("direct:notification");

	from("direct:notification").routeId("notification")
	.log("fired notification")
	.to("stream:out");
    }

}
