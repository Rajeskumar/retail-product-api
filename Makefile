
.DEFAULT_GOAL := help

help:
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) \
	| sed -n 's/^\(.*\): \(.*\)##\(.*\)/\1\3/p' \
#	| column -t  -s ' '

test: ## - Runs Unit and Integration test
	 ./gradlew test

build: ## - Builds the application
	./gradlew clean build

run-cassandra: ## - Setup cassandra in local using docker
	cd docker && docker-compose build && docker-compose up

run: build ## - Builds and Runs the Spring boot application
	./gradlew bootRun