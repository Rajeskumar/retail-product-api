
test:
	 ./gradlew test

build:
	./gradlew clean build

run-cassandra:
	cd docker && docker-compose build && docker-compose up

run: build
	./gradlew bootRun