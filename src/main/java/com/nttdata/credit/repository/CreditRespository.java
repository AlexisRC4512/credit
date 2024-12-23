package com.nttdata.credit.repository;

import com.nttdata.credit.model.entity.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRespository extends ReactiveMongoRepository<Credit,String> {
}
