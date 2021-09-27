# paymentlcmel
POC on Payment Life Cycle Management Services built using Apache Camel.

Beware!! This wasn't meant for production use !!!

## Prerequisites

1. OpenJDK 11. 
2. Maven
3. Docker

## Running the Project

1. Clone this project
2. Running the database and Payment dummy services by executing docker-compose file.
```
cd <project directory>
docker-compose up
```
3. Run the service using these following steps:
   - Compile 
     ```
     mvn clean install
     ```
   - Run
     ```
     mvn spring-boot:run
     ```
   
## Test the Endpoint

```
curl -X GET -H 'Accept: application/json' http://localhost:8080/lcm/payments/{service_id}/{merchant_id}/{customer_id}
```

You can get the `service_id`, `merchant_id`, and `customer_id` from the database. 
