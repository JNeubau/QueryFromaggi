# QueryFromaggi
A Pizza Place App for Uni Class

## Project Overview

This project is a microservices-based application for a pizza place. It is designed to handle various aspects of the
pizza ordering process, from placing an order to delivering the pizza and processing payments.

## Microservices

### 1. Ordering Food

This microservice handles the process of placing an order. It provides a RESTful interface for customers to select their
desired pizzas and place an order.

### 2. Preparation of Pizza

This microservice manages the preparation of pizzas. It provides a RESTful interface that tracks the status of each
order.

### 3. Payment

This microservice (SOAP Service) processes payments for orders. It handles transactions securely and ensures that
payments are completed successfully.

## Communication

The microservices communicate with each other through a message broker to ensure seamless integration and coordination.

## API Gateway

The API Gateway provides a RESTful interface described using OpenAPI. It includes two operations:

1. **Place Order**: Allows customers to place a new order.
2. **Get Order Status**: Allows customers to check the status of their order.

[//]: # (## Saga Pattern)

[//]: # (The application implements the saga pattern to ensure eventual consistency in the processing of orders.)

[//]: # (## Web Application)

[//]: # (A web application with a graphical user interface is provided to allow customers to place orders and check their status. This application interacts with the microservices to provide a seamless user experience.)

## Containerization

The entire application, including the microservices and the web application, is containerized to facilitate deployment
and scalability.

http://localhost:8090/webjars/swagger-ui/index.html?url=/api/api-doc

```bash
docker-compose logs --follow --tail=0

docker compose up -d
```