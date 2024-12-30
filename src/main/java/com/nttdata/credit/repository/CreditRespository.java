package com.nttdata.credit.repository;

import com.nttdata.credit.model.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CreditRespository extends ReactiveMongoRepository<Credit,String> {
    Flux<Credit> findByClientId(String id);

}
