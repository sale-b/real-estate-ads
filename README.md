# REAL ESTATE ADS

## Overview

This project showcases an elegant OAuth2 authorization flow implemented with Spring Authorization Server, Spring OAuth
Resource Server, and Spring OAuth Client. Additionally, it integrates a robust notification service that generates
real-time notifications triggered by various events related to creating advertisements and applying filters.

### Project Components

The following figure shows the system architecture.

![img1](https://i.ibb.co/GH1ytyz/Screenshot-2023-09-03-191153.jpg)

#### 1. Spring Authorization Server

Purpose: The Spring Authorization Server serves as the cornerstone of this project's security infrastructure. It handles
user authentication, issues access tokens, and enforces access control policies. It ensures that only authorized users
can access protected resources.

#### 2. Spring OAuth Resource Server

Purpose: The Spring OAuth Resource Server acts as the gateway to protected resources, such as user data and
advertisements. It validates incoming OAuth2 access tokens, authorizes requests, and provides secure access to these
resources.

#### 3. Spring OAuth Client

Purpose: The Spring OAuth Client represents the client application that interacts with the OAuth2 authorization server
to obtain access tokens. It demonstrates the process of acquiring and using tokens to access protected resources
securely.

#### 4. Notification Service

The project features a sophisticated Notification Service that handles the generation of notifications based on key
events, related to the creation of advertisements and the application of filters by users. This service ensures that
users stay informed in real-time about relevant updates.

### Communication Protocols

#### 1. ActiveMQ Communication

The project employs ActiveMQ, a powerful message broker, for asynchronous communication between components. It enables
efficient event-driven messaging and ensures seamless interaction between various parts of the system.

#### 2. HTTP (REST) Communication

The project leverages RESTful HTTP communication for traditional client-server interactions. It provides a structured
and predictable way for client applications to request and manipulate resources from the server.

#### 3. WebSocket Communication

WebSocket communication is utilized for real-time, bidirectional interactions. It enables instant updates and
notifications, making the application responsive and engaging for users.

## Getting Started
To run the project locally or in your development environment, follow these steps:

#### 1. Clone the Repository: 
Start by cloning this repository to your local machine using Git.

```git clone https://github.com/sale-b/real-estate-ads.git```

#### 2. Required Dependencies: 
Ensure you have the following dependencies installed on your system:

- Docker: Install Docker to manage containerized applications.

- Maven: Install Maven to build the project modules.

#### 3. Build Maven Modules: 
Navigate to the project's root directory and build all four Maven modules: **auth server, client server, resource server, and notification service** using the following commands:

```bash
cd authorization-server
mvn clean install
```

#### 4. Modify /etc/hosts File (Windows/Linux/macOS):

To ensure seamless communication between components both inside and outside Docker containers, add the following entries to your /etc/hosts file:

```
127.0.0.1 auth-server
127.0.0.1 client-server
127.0.0.1 resource-server
127.0.0.1 db
127.0.0.1 active-mq
```
*On Windows, you can add these entries to your C:\Windows\System32\drivers\etc\hosts file.*

These entries ensure that your application works correctly, whether running with or without Docker.

#### 5. Run Docker Compose: 
Start the application by running Docker Compose. Navigate to the project's root directory and execute the following command:

```bash
docker-compose up
```
This command launches the application components, including the authorization server, client server, resource server, and notification service, along with required dependencies such as the database and ActiveMQ.

#### 6. Explore the Application: 
Access the application by opening your web browser and visiting `http://client-server:8081`. This URL will take you to the client server, where you can begin exploring and interacting with the Real Estate Ads application.