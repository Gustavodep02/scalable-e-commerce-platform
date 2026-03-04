# 🛒 Scalable E-Commerce Platform

A scalable e-commerce platform built with **microservices architecture** using **Spring Boot**, **Docker**, and **Kubernetes**. The platform handles product catalog management, user authentication, shopping cart, payment processing, order management, and email notifications — each as an independent microservice.

> 📌 This project is based on the [Scalable E-Commerce Platform](https://roadmap.sh/projects/scalable-ecommerce-platform) challenge from [roadmap.sh](https://roadmap.sh).

---

## 📐 Architecture Overview

```
                         ┌──────────────┐
                         │  API Gateway │ (Spring Cloud Gateway)
                         │   :8080      │
                         └──────┬───────┘
                                │
                         ┌──────┴───────┐
                         │    Eureka    │ (Service Discovery)
                         │    :8761     │
                         └──────┬───────┘
                                │
        ┌───────────┬───────────┼───────────┬───────────┬───────────┐
        │           │           │           │           │           │
  ┌─────┴─────┐ ┌───┴───┐ ┌────┴────┐ ┌────┴────┐ ┌────┴────┐ ┌───┴────────┐
  │   User    │ │Product│ │  Cart   │ │  Order  │ │ Payment │ │Notification│
  │  Service  │ │Service│ │ Service │ │ Service │ │ Service │ │  Service   │
  │   :8081   │ │ :8082 │ │  :8083  │ │  :8084  │ │  :8085  │ │   :8086    │
  └─────┬─────┘ └───┬───┘ └────┬────┘ └────┬────┘ └────┬────┘ └─────┬──────┘
        │           │          │           │           │             │
      [DB]        [DB]       [DB]        [DB]        [DB]          [DB]
    PostgreSQL  PostgreSQL PostgreSQL  PostgreSQL  PostgreSQL    PostgreSQL
```

**Event-driven communication** between services via **Apache Kafka** (e.g., order created → payment processing → notification).

---

## 🧰 Technologies Used

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Cloud Gateway](https://img.shields.io/badge/Spring_Cloud_Gateway-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Eureka](https://img.shields.io/badge/Netflix_Eureka-E50914?style=for-the-badge&logo=netflix&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)
![Kibana](https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=kibana&logoColor=white)
![Logstash](https://img.shields.io/badge/Logstash-005571?style=for-the-badge&logo=logstash&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-635BFF?style=for-the-badge&logo=stripe&logoColor=white)
![SendGrid](https://img.shields.io/badge/SendGrid-1A82E2?style=for-the-badge&logo=sendgrid&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

---

## 🛠️ Tech Stack

| Category               | Technology                             |
|------------------------|----------------------------------------|
| **Language**           | Java 17                                |
| **Framework**          | Spring Boot 4                          |
| **API Gateway**        | Spring Cloud Gateway                   |
| **Service Discovery**  | Netflix Eureka                         |
| **Inter-Service Comm** | OpenFeign (REST), Apache Kafka (async events) |
| **Authentication**     | JWT (JSON Web Tokens) with role-based access |
| **Payments**           | Stripe API integration                 |
| **Notifications**      | Twilio SendGrid (email)                |
| **Databases**          | PostgreSQL 16 (one per service)        |
| **Monitoring**         | Prometheus + Grafana                   |
| **Logging**            | ELK Stack (Elasticsearch, Logstash, Kibana) |
| **API Docs**           | Swagger / SpringDoc OpenAPI            |
| **Health Checks**      | Spring Boot Actuator                   |
| **Containerization**   | Docker + Docker Compose                |
| **Orchestration**      | Kubernetes (Kind)                      |
| **Testing**            | JUnit 5, Mockito, H2 (in-memory)       |
| **CI/CD**              | GitHub Actions                         |

---

## 🏗️ Microservices

| Service                  | Port   | Description                                                          |
|--------------------------|--------|----------------------------------------------------------------------|
| **API Gateway**          | `8080` | Entry point for all requests, routes to services via Eureka          |
| **Eureka Server**        | `8761` | Service discovery and registration                                   |
| **User Service**         | `8081` | User registration, authentication (JWT), profile management          |
| **Product Service**      | `8082` | Product catalog CRUD and inventory management                        |
| **Cart Service**         | `8083` | Shopping cart management (add/remove items, update quantities)        |
| **Order Service**        | `8084` | Order placement, tracking, and history                               |
| **Payment Service**      | `8085` | Payment processing with Stripe integration                           |
| **Notification Service** | `8086` | Email notifications via SendGrid (order created, paid, cancelled)    |

---

## 📦 Infrastructure

| Component         | Port   | Description                        |
|-------------------|--------|------------------------------------|
| PostgreSQL        | Various| One database per microservice      |
| Apache Kafka      | `9092` | Event streaming between services   |
| Zookeeper         | `2181` | Kafka cluster management           |
| Elasticsearch     | `9200` | Log storage and search             |
| Logstash          | `5000` | Log aggregation and processing     |
| Kibana            | `5601` | Log visualization dashboard        |
| Prometheus        | `9090` | Metrics collection                 |
| Grafana           | `3000` | Metrics visualization dashboard    |

---

## 🚀 Getting Started

### Prerequisites

- **Docker** and **Docker Compose**
- **Java 17** (for local development)
- **Stripe** account (for payments)
- **SendGrid** account (for email notifications)
- **ngrok** (to expose webhook endpoint for Stripe)

### Option 1: Docker Compose

1. **Clone the repository**

```bash
git clone https://github.com/Gustavodep02/scalable-e-commerce-platform.git
cd scalable-e-commerce-platform
```

2. **Configure environment variables**

```bash
cp .env.example .env
```

Edit `.env` and fill in your credentials:
- Database passwords
- `JWT_SECRET` — any secret string for JWT signing
- `STRIPE_SK` — your Stripe Secret Key
- `STRIPE_WEBHOOK_SECRET` — your Stripe Webhook Secret
- `SENDGRID_API_KEY` — your SendGrid API Key
- `SENDGRID_FROM_EMAIL` — your verified SendGrid sender email

3. **Start ngrok** (required for Stripe webhooks)

```bash
ngrok http 8080
```

Copy the HTTPS URL and configure it as a webhook endpoint in your Stripe Dashboard → Webhooks → `https://<ngrok-url>/api/payments/webhook`.

4. **Run the platform**

```bash
docker compose up --build
```

5. **Access the services**

- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- Swagger (per service): http://localhost:{port}/swagger-ui.html
- Grafana: http://localhost:3000
- Kibana: http://localhost:5601
- Prometheus: http://localhost:9090

---

### Option 2: Kubernetes (Kind)

#### Prerequisites

- **Docker**
- **[Kind](https://kind.sigs.k8s.io/)** — Kubernetes in Docker
- **kubectl**

#### Steps

1. **Configure Kubernetes secrets and config**

```bash
cp k8s/secret.yaml.example k8s/secret.yaml
cp k8s/configmap.yaml.example k8s/configmap.yaml
```

Edit `k8s/secret.yaml` with your credentials and `k8s/configmap.yaml` with your configuration.

2. **Deploy everything**

```bash
bash k8s/deploy-all.sh
```

This script will:
- Create a Kind cluster with 1 control-plane + 3 worker nodes
- Build all Docker images
- Load images into the Kind cluster
- Apply ConfigMap and Secrets
- Deploy infrastructure (Kafka, Zookeeper, ELK, Prometheus, Grafana)
- Deploy all databases
- Deploy Eureka
- Deploy all microservices
- Deploy the API Gateway

3. **Access the services via port-forward**

```bash
kubectl port-forward svc/api-gateway 8080:80
kubectl port-forward svc/eureka 8761:8761
kubectl port-forward svc/grafana 3000:3000
kubectl port-forward svc/kibana 5601:5601
```

4. **Check deployment status**

```bash
kubectl get pods
kubectl get svc
```

5. **Cleanup**

```bash
kind delete cluster
```

---

## 📡 API Endpoints

All requests go through the **API Gateway** at `http://localhost:8080`.

### Authentication

| Method | Endpoint              | Description          |
|--------|-----------------------|----------------------|
| POST   | `/api/users/register` | Register a new user  |
| POST   | `/api/users/login`    | Login and get JWT    |

### Products

| Method | Endpoint            | Description              |
|--------|---------------------|--------------------------|
| GET    | `/api/products`     | List all products        |
| GET    | `/api/products/{id}`| Get product by ID        |
| POST   | `/api/products`     | Create a product (AUTH)  |
| PUT    | `/api/products/{id}`| Update a product (AUTH)  |
| DELETE | `/api/products/{id}`| Delete a product (AUTH)  |

### Cart

| Method | Endpoint                | Description              |
|--------|-------------------------|--------------------------|
| GET    | `/api/cart`             | Get user's cart           |
| POST   | `/api/cart/items`       | Add item to cart          |
| PUT    | `/api/cart/items/{id}`  | Update item quantity      |
| DELETE | `/api/cart/items/{id}`  | Remove item from cart     |

### Orders

| Method | Endpoint           | Description              |
|--------|--------------------|--------------------------|
| POST   | `/api/orders`      | Place an order            |
| GET    | `/api/orders`      | Get user's orders         |
| GET    | `/api/orders/{id}` | Get order by ID           |

### Payments

| Method | Endpoint                | Description                  |
|--------|-------------------------|------------------------------|
| POST   | `/api/payments/webhook` | Stripe webhook endpoint      |

> 📝 For full API documentation, access Swagger UI at `http://localhost:{service-port}/swagger-ui.html` after starting the services.

---
## 🔄 CI/CD Pipeline

The project uses **GitHub Actions** with a pipeline that runs automatically on every push to `main`.

The pipeline:
1. Checks out the source code
2. Sets up Java 17
3. Builds and tests all microservices in parallel (matrix strategy)
4. Logs in to Docker Hub
5. Builds and pushes Docker images tagged with `latest` and the commit SHA

**Secrets required** (configure in GitHub → Settings → Secrets):

| Secret            | Description                    |
|-------------------|--------------------------------|
| `DOCKER_USERNAME` | Your Docker Hub username       |
| `DOCKER_PASSWORD` | Your Docker Hub access token   |

After a successful pipeline run, all images are available publicly on Docker Hub at:

| Service                  | Image                                                                                          |
|--------------------------|------------------------------------------------------------------------------------------------|
| **API Gateway**          | [gustavodp02/api-gateway](https://hub.docker.com/r/gustavodp02/api-gateway)                   |
| **Eureka Server**        | [gustavodp02/eureka](https://hub.docker.com/r/gustavodp02/eureka)                             |
| **User Service**         | [gustavodp02/user-service](https://hub.docker.com/r/gustavodp02/user-service)                 |
| **Product Service**      | [gustavodp02/product-service](https://hub.docker.com/r/gustavodp02/product-service)           |
| **Cart Service**         | [gustavodp02/cart-service](https://hub.docker.com/r/gustavodp02/cart-service)                 |
| **Order Service**        | [gustavodp02/order-service](https://hub.docker.com/r/gustavodp02/order-service)               |
| **Payment Service**      | [gustavodp02/payment-service](https://hub.docker.com/r/gustavodp02/payment-service)           |
| **Notification Service** | [gustavodp02/notification-service](https://hub.docker.com/r/gustavodp02/notification-service) |

> 🐳 All images: https://hub.docker.com/repositories/gustavodp02

To pull an image manually:
```bash
docker pull gustavodp02/<service-name>:latest
```

---

## 📊 Monitoring & Logging

### Prometheus + Grafana

Each microservice exposes metrics via **Spring Boot Actuator** at `/actuator/prometheus`. Prometheus scrapes these endpoints and Grafana visualizes them.

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

### ELK Stack

All microservices send logs to **Logstash**, which indexes them in **Elasticsearch**. Use **Kibana** to search and visualize logs.

- Kibana: http://localhost:5601

---
