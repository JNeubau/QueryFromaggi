package org.bp.payment;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Servlet;

@SpringBootApplication
public class MicroPizzaOrderingApplication {

//	@Bean
//	public ServletRegistrationBean<Servlet> servletRegistrationBean() {
//		ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<>(new CamelHttpTransportServlet(), "/camel/*");
//		registration.setName("CamelServlet");
//		return registration;
//	}

	public static void main(String[] args) {
		SpringApplication.run(MicroPizzaOrderingApplication.class, args);
	}

}
