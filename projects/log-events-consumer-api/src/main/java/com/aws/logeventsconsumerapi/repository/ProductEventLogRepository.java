package com.aws.logeventsconsumerapi.repository;

import com.aws.logeventsconsumerapi.dto.event.dynamodb.ProductEventKey;
import com.aws.logeventsconsumerapi.dto.event.dynamodb.ProductEventLog;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ProductEventLogRepository extends CrudRepository<ProductEventLog, ProductEventKey> {

    List<ProductEventLog> findAllByPk(String code);
    List<ProductEventLog> findAllByPkAndSkStartsWith(String code, String eventType);
}
