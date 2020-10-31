package com.retail.productapi.dao;

import com.retail.productapi.domain.ProductPriceData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceRepository extends CassandraRepository<ProductPriceData, Integer> {

}
