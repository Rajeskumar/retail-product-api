# Retail Product API
_Current Version: 1.0.0_

## **Introduction**

_Repository that holds product api source which aggregates product details from multiple sources._

## Tech Stack
Spring Boot, Java 11, Spock, Groovy, Cassandra, Gradle, Docker, Swagger2.


## Getting Started

#### Pre-requisites
* Cassandra
* Docker
* Java 11

#### Clone repository

    git clone https://github.com/Rajeskumar/retail-product-api.git
 

### Deploy app using Docker Container

_Note: This will spin up Cassandra container and application container_

    make deploy

#### Swagger Endpoint
<http://localhost:8080/swagger-ui.html>

#### Product API

* Get Product

```curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/v1/product/13264003' ```

* Update ProductPrice

```
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \ 
      "id": 13264003, \ 
      "name": "Jif Natural Creamy Peanut Butter - 40oz", \ 
      "current_price": { \ 
        "value": 10.99, \ 
        "currency_code": "USD" \ 
      } \ 
    }' 'http://localhost:8080/api/v1/product/13264003'

```
#### Cassandra Local Setup
_Note: Run this, if want to run cassandra for debugging_

    make run-cassandra

#### Run Test
_Note: Cassandra should be up_

    make test

#### Run app
_Note: This is to run the app directly, update cassandra properties appropriately_

    make run
    