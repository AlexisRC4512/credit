package com.nttdata.credit.service.impl;

import com.nttdata.credit.model.entity.Credit;
import com.nttdata.credit.model.exception.CreditNotFoundException;
import com.nttdata.credit.model.exception.InvalidCreditDataException;
import com.nttdata.credit.model.request.CreditRequest;
import com.nttdata.credit.model.response.CreditResponse;
import com.nttdata.credit.repository.CreditRespository;
import com.nttdata.credit.service.CreditService;
import com.nttdata.credit.util.CreditConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * Implementation of the credit service.
 */
@Service
@Slf4j
public class CreditServiceImpl implements CreditService {
    @Autowired
    private CreditRespository creditRespository;
    /**
     * Retrieves all credits.
     *
     * @return a flux of credit responses.
     */
    @Override
    public Flux<CreditResponse> getAllCredits() {
        log.info("Fetching all Credits");
        return creditRespository.findAll()
                .map(CreditConverter::toCreditResponse)
                .onErrorMap(e -> new Exception("Error fetching all Credits", e));
    }
    /**
     * Retrieves a credit by its ID.
     *
     * @param idCredit the credit ID.
     * @return a credit response.
     */
    @Override
    public Mono<CreditResponse> getCreditById(String idCredit) {
        log.debug("Fetching Credit with id: {}", idCredit);
        return creditRespository.findById(idCredit)
                .map(CreditConverter::toCreditResponse)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit not found with id: " + idCredit)))
                .onErrorMap(e -> new Exception("Error fetching Credit by id", e));
    }
    /**
     * Creates a new credit.
     *
     * @param creditRequest the credit request.
     * @return a credit response.
     */
    @Override
    public Mono<CreditResponse> createCredit(CreditRequest creditRequest) {
        if (creditRequest == null ) {
            log.warn("Invalid Credit data: {}", creditRequest);
            return Mono.error(new InvalidCreditDataException("Invalid Credit data"));
        }
        log.info("Creating new Credit: {}", creditRequest.getType().name());
        Credit credit = CreditConverter.toCredit(creditRequest);
        return creditRespository.save(credit)
                .map(CreditConverter::toCreditResponse)
                .doOnError(e -> log.error("Error creating Credit", e))
                .onErrorMap(e -> new Exception("Error creating Credit", e));
    }


    /**
     * Updates an existing credit.
     *
     * @param id            the credit ID.
     * @param creditRequest the credit request.
     * @return a credit response.
     */
    @Override
    public Mono<CreditResponse> updateCredit(String id, CreditRequest creditRequest) {
        if (creditRequest == null ) {
            log.warn("Invalid Credit data for update: {}", creditRequest);
            return Mono.error(new InvalidCreditDataException("Invalid Credit data"));
        }
        log.debug("Updating Credit with id: {}", id);
        return creditRespository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit not found with id: " + id)))
                .flatMap(existingClient -> {
                    Credit updateCredit = CreditConverter.toCredit(creditRequest);
                    updateCredit.setId(existingClient.getId());
                    return creditRespository.save(updateCredit);
                })
                .map(CreditConverter::toCreditResponse)
                .onErrorMap(e -> new Exception("Error updating Credit", e));
    }
    /**
     * Deletes a credit by its ID.
     *
     * @param id the credit ID.
     * @return a void Mono.
     */
    @Override
    public Mono<Void> deleteCredit(String id) {
        log.debug("Deleting Credit with id: {}", id);
        return creditRespository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit not found with id: " + id)))
                .flatMap(existingClient -> creditRespository.delete(existingClient))
                .onErrorMap(e -> new Exception("Error deleting Credit", e));
    }
}
