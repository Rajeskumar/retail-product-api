package com.retail.productapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.contact-points:placeholder}")
    private String contactPoints;

    @Value("${cassandra.port:0000}")
    private int port;

    @Value("${cassandra.keyspace:placeholder}")
    private String keySpace;


    /**
     * Return the name of the keyspace to connect to.
     *
     * @return must not be {@literal null}.
     */
    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    public String getContactPoints() {
        return contactPoints;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    protected boolean getMetricsEnabled() { return false; }
}
