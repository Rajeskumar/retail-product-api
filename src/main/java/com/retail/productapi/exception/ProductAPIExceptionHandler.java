package com.retail.productapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.CassandraConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductAPIExceptionHandler  {

    Logger logger = LoggerFactory.getLogger(ProductAPIExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException (BadRequestException ex){
        logger.error("Invalid Request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Product request");
    }

    @ExceptionHandler(CassandraConnectionFailureException.class)
    public ResponseEntity<Object> handleCassandraFailureException (CassandraConnectionFailureException ex){
        logger.error("Cassandra host not available", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Datastore host not available");
    }
}
