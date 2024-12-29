package com.nttdata.credit.service.impl;

import com.nttdata.credit.model.entity.Client;
import com.nttdata.credit.model.entity.Credit;
import com.nttdata.credit.model.entity.Payment;
import com.nttdata.credit.model.exception.CreditNotFoundException;
import com.nttdata.credit.model.exception.InvalidCreditDataException;
import com.nttdata.credit.model.exception.PaymentDataException;
import com.nttdata.credit.model.request.CreditRequest;
import com.nttdata.credit.model.request.PaymentRequest;
import com.nttdata.credit.model.response.CreditResponse;
import com.nttdata.credit.model.response.PaymentResponse;
import com.nttdata.credit.repository.CreditRespository;
import com.nttdata.credit.service.ClientService;
import com.nttdata.credit.service.CreditService;
import com.nttdata.credit.strategy.ValidationStrategy;
import com.nttdata.credit.util.CreditConverter;
import com.nttdata.credit.util.PaymentConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of the credit service.
 */
@Service
@Slf4j
public class CreditServiceImpl implements CreditService {
    @Autowired
    private CreditRespository creditRespository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ValidationStrategy validationStrategy;
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
        Mono<Client> clientMono = clientService.getClientById(credit.getClientId());
        return clientMono.flatMap(client -> validateAndSaveAccount(client, credit))
                .switchIfEmpty(Mono.error(new CreditNotFoundException("credit not found with Client id: ")))
                .doOnError(e -> log.error("Error creating credit", e))
                .onErrorMap(e -> new Exception("Error creating credit", e));
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

    @Override
    public Mono<PaymentResponse> payByCreditId(String id, PaymentRequest paymentRequest) {
        return creditRespository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundException("Credit not found with id: " + id)))
                .flatMap(credit -> Mono.justOrEmpty(paymentRequest)
                        .switchIfEmpty(Mono.defer(() -> {
                            log.warn("Invalid Payment data: {}", paymentRequest);
                            return Mono.error(new PaymentDataException("Invalid Payment data"));
                        }))
                        .flatMap(req -> {
                            double newOutstandingBalance = credit.getOutstandingBalance() - req.getAmount();
                            if (newOutstandingBalance < 0) {
                                log.warn("Payment amount exceeds outstanding balance: {}", req.getAmount());
                                return Mono.error(new PaymentDataException("Payment amount exceeds outstanding balance"));
                            }
                            credit.setOutstandingBalance(Math.round(newOutstandingBalance));
                            Payment payment = new Payment(req.getAmount(), new Date(), "new Pay");
                            Optional.ofNullable(credit.getPayments())
                                    .orElseGet(() -> {
                                        List<Payment> payments = new ArrayList<>();
                                        credit.setPayments(payments);
                                        return payments;
                                    })
                                    .add(payment);

                            return creditRespository.save(credit)
                                    .then(Mono.just(PaymentConverter.toPaymentResponse(payment)));
                        })
                )
                .doOnError(e -> log.error("Error creating Payment", e))
                .onErrorMap(e -> new Exception("Error creating Payment", e));
    }

    @Override
    public Flux<PaymentResponse> getAllPaysByCredirId(String id) {
        return creditRespository.findById(id)
                .flatMapMany(credit -> {
                    List<PaymentResponse> paymentResponses = credit.getPayments().stream()
                            .map(PaymentConverter::toPaymentResponse)
                            .collect(Collectors.toList());
                    return Flux.fromIterable(paymentResponses);
                });
    }
    private Mono<CreditResponse> validateAndSaveAccount(Client client, Credit credit) {
        String clientType = client.getType();
        Function<Credit, Mono<CreditResponse>> validationFunction = validationStrategy.validationStrategies.get(clientType);

        if (validationFunction != null) {
            return validationFunction.apply(credit);
        } else {
            return Mono.error(new CreditNotFoundException("Unknown client type"));
        }
    }
}
