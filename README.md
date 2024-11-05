# Ordering System

## Table of Contents
1. [Project Overview](#project-overview)
2. [Services Description](#services-description)
    - [Authorization/Authentication Service](#authorizationauthentication-service)
    - [Order Service](#order-service)
    - [Notification Service](#notification-service)
    - [Packaging and Delivery Service](#packaging-and-delivery-service)
    - [Products Service](#products-service)
3. [Usage](#usage)

## Project Overview
The Ordering System is a microservices-based application designed to facilitate user authentication, order management, notifications, product handling, and packaging and delivery processes. The system comprises five main services: Authorization/Authentication Service, Order Service, Notification Service, Packaging and Delivery Service, and Products Service.

## Services Description

### Authorization/Authentication Service
This service is responsible for managing user accounts and their authentication processes.

#### Functionalities:
- **User Management:**
    - **CRUD Operations for Users:** Users can view and modify their details. Administrators can perform CRUD operations for all users.
    - **Sign Up:** Registration process for clients, granting them default roles.
- **Sign In:** Allows users to log into the system.
- **System Roles:**
    - **Administrator:** Has full access and can manage all users.
    - **Seller:** Assigned only by administrators.
    - **Client:** Default role upon registration.

### Order Service
This service acts as the main entry point for clients to place orders.

#### Functionalities:
- **Order Placement:** Clients can place orders through this service.
- **Message Passing:**
    - Upon order placement, the service communicates with:
        - **Notification Service:** To notify the seller and client.
        - **Packaging and Delivery Service:** To update and store the order status:
            - **Pending**
            - **Delivered**
            - **Canceled**
        - **Products Service:** To reduce the quantities of the purchased products.

### Notification Service
This service manages the communication with users regarding their orders.

#### Functionalities:
- **Email Notifications:** Sends emails to the seller with information about the ordered products.
- **SMS Notifications:** Sends SMS to the client with order details, including order ID and total price.

### Packaging and Delivery Service
This service handles the details related to order packaging and delivery.

#### Functionalities:
- **Order and Product Information Storage:** Stores information about placed orders, products, and their statuses:
    - **Pending**
    - **Delivered**
    - **Canceled**
- **Delivery List Retrieval:** Returns a list of deliveries based on filters such as date range and amount range.

### Products Service
This service is responsible for managing product information.

#### Functionalities:
- **Product Retrieval:** Returns a list of products, including their prices, quantities, and categories.
- **Category Retrieval:** Returns a list of product categories.

## Usage
### Prerequisites
- **Java 17**
- **Docker**
- **PostgreSQL**

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ordering-system.git
   cd ordering-system
   ```
2. Build the services with Maven:
   ```bash
   mvn clean install
   ```

### 3. **Running the Services**
```markdown
### Running the Services

**Using Docker Compose:**
1. Ensure Docker is running.
2. From the root directory, start the services with Docker Compose:
   ```bash
   docker-compose up
   ```

**Running Manually (without Docker):**
- Start PostgreSQL and make sure it's connected.
- Start each service from its respective directory:
  ```bash
  cd authorizationservice
  mvn spring-boot:run

  cd ../orderservice
  mvn spring-boot:run
  
  cd ../notificationservice
  mvn spring-boot:run
  
  cd ../packaginganddeliveryservice
  mvn spring-boot:run
  
  cd ../productsservice
  mvn spring-boot:run
  ```

### API Endpoints

#### Authorization/Authentication Service
- **POST /api/auth/signup**: Register a new client.
- **POST /api/auth/signin**: Log in with username and password.
- **GET /api/auth/users**: Retrieve a list of all users (Admin only).
- **GET /api/auth/me**: Get details of the currently authenticated user or specified user (Admins can view any user).
- **PUT /api/auth/me**: Update details of the currently authenticated user or specified user (Admins can update any user).
- **DELETE /api/auth/me**: Delete the account of the currently authenticated user or specified user (Admins can delete any user).
- **PUT /api/auth/admin/assign-seller/{username}**: Assign the seller role to a user (Admin only).

#### Order Service
- **POST /api/orders**: Place a new order for the authenticated client.

#### Notification Service
- **(Triggered internally)**: Sends notifications based on order placement.

#### Packaging and Delivery Service
- **GET /api/delivery/status**: Retrieve a list of orders filtered by status.
- **GET /api/delivery/date-range**: Retrieve a list of orders filtered by a specified date range.

#### Products Service
- **GET /api/products**: Retrieve a list of all products.
- **GET /api/categories**: Retrieve a list of all categories.

### Testing

**Using Swagger:**
- Access Swagger UI at `http://localhost:8081/swagger-ui/index.html` to test the Authorization/Authentication API.
- Access Swagger UI at `http://localhost:8082/swagger-ui/index.html` to test the Order API.
- Access Swagger UI at `http://localhost:8084/swagger-ui/index.html` to test the Packaging and Delivery API.
- Access Swagger UI at `http://localhost:8085/swagger-ui/index.html` to test the Products API.