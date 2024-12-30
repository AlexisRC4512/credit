package com.nttdata.credit.service;

import com.nttdata.credit.model.request.CreditRequest;
import com.nttdata.credit.model.request.PaymentRequest;
import com.nttdata.credit.model.response.CreditResponse;
import com.nttdata.credit.model.response.PaymentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Flux<CreditResponse>getAllCredits();
    Mono<CreditResponse>getCreditById(String idCredit);
    Mono<CreditResponse> createCredit(CreditRequest creditRequest);
    Mono<CreditResponse> updateCredit(String id, CreditRequest creditRequest);
    Mono<Void> deleteCredit(String id);
    Mono<PaymentResponse>payByCreditId(String id , PaymentRequest paymentRequest);
    Flux<PaymentResponse>getAllPaysByCredirId(String id);
}
