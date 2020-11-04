
.DEFAULT_GOAL := help

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) \
	| sed -n 's/^\(.*\): \(.*\)##\(.*\)/\1\3/p' \
#	| column -t  -s ' '

clean: ## - Clean gradle build
	./gradlew clean

test: ## - Runs Unit and Integration test
	 ./gradlew test

build: ## - Builds the application
	./gradlew clean build -x test

run-cassandra: ## - Setup cassandra in local using docker
	cd docker && docker-compose build cassandra && docker-compose up -d cassandra

run: build ## - Builds and Runs the Spring boot application
	./gradlew bootRun

deploy: build ## - Deploys app in docker container
	./gradlew copyBuiltJar
	cd docker && docker-compose build && docker-compose up -d
	rm -rf docker/retail-product-app/product-api*.jar