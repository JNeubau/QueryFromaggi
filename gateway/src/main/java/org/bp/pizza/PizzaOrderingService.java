package org.bp.pizza;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.bp.pizza.model.PizzaInfo;
import org.bp.pizza.model.PizzaOrderRequest;
import org.bp.pizza.model.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;

@Component
public class PizzaOrderingService extends RouteBuilder {

    @Autowired
    PizzaIdentifierService pizzaIdentifierService;

//	@Autowired
//	PaymentService paymentService;
//
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
//		hotel();
//		flight();
//		payment();
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
                .process((exchange) -> {
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

//	private void hotel() {
//		from("kafka:TravelReqTopic?brokers=localhost:9092").routeId("bookHotel")
//		.log("fired bookHotel")
//		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
//		.process(
//				(exchange) -> {
//					String bookingTravelId = exchange.getMessage().getHeader("bookingTravelId", String.class);
//					ProcessingState previousState = hotelStateService.sendEvent(bookingTravelId, ProcessingEvent.START);
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
//		    			previousState = hotelStateService.sendEvent(bookingTravelId, ProcessingEvent.FINISH);
//		    			}
//					exchange.getMessage().setHeader("previousState", previousState);        		}
//				)
//		.marshal().json()
//		.to("stream:out")
//		.choice()
//			.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
//			.to("direct:bookHotelCompensationAction")
//		.otherwise()
//			.setHeader("serviceType", constant("hotel"))
//			.to("kafka:BookingInfoTopic?brokers=localhost:9092")
//		.endChoice()
//		;
//
//		from("kafka:TravelBookingFailTopic?brokers=localhost:9092").routeId("bookHotelCompensation")
//		.log("fired bookHotelCompensation")
//		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
//		.choice()
//			.when(header("serviceType").isNotEqualTo("hotel"))
//		    .process((exchange) -> {
//				String bookingTravelId = exchange.getMessage().getHeader("bookingTravelId", String.class);
//				ProcessingState previousState = hotelStateService.sendEvent(bookingTravelId, ProcessingEvent.CANCEL);
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
//	}
//
//	private void flight() {
//		from("kafka:TravelReqTopic?brokers=localhost:9092").routeId("bookFlight")
//		.log("fired bookFlight")
//		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
//		.process(
//				(exchange) -> {
//					String bookingTravelId = exchange.getMessage().getHeader("bookingTravelId", String.class);
//					ProcessingState previousState = flightStateService.sendEvent(bookingTravelId, ProcessingEvent.START);
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
//		    			previousState = flightStateService.sendEvent(bookingTravelId, ProcessingEvent.FINISH);
//		    			}
//					exchange.getMessage().setHeader("previousState", previousState);
//				}
//				)
//		.marshal().json()
//		.to("stream:out")
//		.choice()
//			.when(header("previousState").isEqualTo(ProcessingState.CANCELLED))
//			.to("direct:bookFlightCompensationAction")
//		.otherwise()
//			.setHeader("serviceType", constant("flight"))
//			.to("kafka:BookingInfoTopic?brokers=localhost:9092")
//		.endChoice()
//		;
//
//		from("kafka:TravelBookingFailTopic?brokers=localhost:9092").routeId("bookFlightCompensation")
//		.log("fired bookFlightCompensation")
//		.unmarshal().json(JsonLibrary.Jackson, ExceptionResponse.class)
//        .choice()
//    		.when(header("serviceType").isNotEqualTo("flight"))
//            .process((exchange) -> {
//    			String bookingTravelId = exchange.getMessage().getHeader("bookingTravelId", String.class);
//    			ProcessingState previousState = flightStateService.sendEvent(bookingTravelId, ProcessingEvent.CANCEL);
//    			exchange.getMessage().setHeader("previousState", previousState);
//            })
//            .choice()
//            	.when(header("previousState").isEqualTo(ProcessingState.FINISHED))
//    			.to("direct:bookFlightCompensationAction")
//    		.endChoice()
//         .endChoice();
//
//		from("direct:bookFlightCompensationAction").routeId("bookFlightCompensationAction")
//		.log("fired bookFlightCompensationAction")
//		.to("stream:out");
//	}
//
//	private void payment() {
//		from("kafka:BookingInfoTopic?brokers=localhost:9092").routeId("paymentBookingInfo")
//		.log("fired paymentBookingInfo")
//		.unmarshal().json(JsonLibrary.Jackson, PizzaInfo.class)
//		.process(
//				(exchange) -> {
//					String bookingTravelId = exchange.getMessage().getHeader("bookingTravelId", String.class);
//					boolean isReady= paymentService.addBookingInfo(
//							bookingTravelId,
//							exchange.getMessage().getBody(PizzaInfo.class),
//							exchange.getMessage().getHeader("serviceType", String.class));
//					exchange.getMessage().setHeader("isReady", isReady);
//				}
//				)
//		.choice()
//			.when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
//		    .endChoice();
//
//		from("kafka:TravelReqTopic?brokers=localhost:9092").routeId("paymentTravelReq")
//		.log("fired paymentTravelReq")
//		.unmarshal().json(JsonLibrary.Jackson, PizzaOrderRequest.class)
//		.process(
//				(exchange) -> {
//					String bookingTravelId = exchange.getMessage().getHeader("bookingTravelId", String.class);
//					 boolean isReady= paymentService.addBookTravelRequest(
//							bookingTravelId,
//							exchange.getMessage().getBody(PizzaOrderRequest.class));
//					 exchange.getMessage().setHeader("isReady", isReady);
//				}
//				)
//		.choice()
//			.when(header("isReady").isEqualTo(true)).to("direct:finalizePayment")
//		.endChoice();
//
//	from("direct:finalizePayment").routeId("finalizePayment")
//	.log("fired finalizePayment")
//	.process(
//			(exchange) -> {
//				String bookingTravelId = exchange.getMessage().getHeader("bookingTravelId", String.class);
//				 PaymentService.PaymentData paymentData = paymentService.getPaymentData(bookingTravelId);
//				 BigDecimal hotelCost=paymentData.hotelPizzaInfo.getCost();
//				 BigDecimal flightCost=paymentData.flightPizzaInfo.getCost();
//				 BigDecimal totalCost=hotelCost.add(flightCost);
//				 PizzaInfo travelBookingInfo = new PizzaInfo();
//				 travelBookingInfo.setId(bookingTravelId);
//				 travelBookingInfo.setCost(totalCost);
//				 exchange.getMessage().setBody(travelBookingInfo);
//			}
//			)
//	.to("direct:notification")
//	;
//
//	from("direct:notification").routeId("notification")
//	.log("fired notification")
//	.to("stream:out");
//
//		}

}
