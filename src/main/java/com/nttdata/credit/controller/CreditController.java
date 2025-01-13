package com.nttdata.credit.controller;

import com.nttdata.credit.model.request.CreditRequest;
import com.nttdata.credit.model.request.PaymentRequest;
import com.nttdata.credit.model.response.BalanceResponse;
import com.nttdata.credit.model.response.CreditResponse;
import com.nttdata.credit.model.response.PaymentResponse;
import com.nttdata.credit.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @GetMapping
    public Flux<CreditResponse> getAllCredits() {
        return creditService.getAllCredits();
    }

    @GetMapping("/{id}")
    public Mono<CreditResponse> getCreditById(@PathVariable String id) {
        return creditService.getCreditById(id);
    }

    @PostMapping
    public Mono<CreditResponse> createCredit(@RequestBody CreditRequest creditRequest) {
        return creditService.createCredit(creditRequest);
    }

    @PutMapping("/{id}")
    public Mono<CreditResponse> updateCredit(@PathVariable String id, @RequestBody CreditRequest creditRequest) {
        return creditService.updateCredit(id, creditRequest);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCredit(@PathVariable String id) {
        return creditService.deleteCredit(id);
    }

    @GetMapping("/{id_client}/balances")
    public Flux<BalanceResponse> getBalanceAccount(@PathVariable("id_client") String idClient) {
        return creditService.getBalanceByClientId(idClient);
    }
    @PostMapping("/{id}/payment")
    public Mono<PaymentResponse> createPayment(@PathVariable String id,@RequestBody PaymentRequest paymentRequest) {
        return creditService.payByCreditId(id,paymentRequest);
    }
    @GetMapping("/{id}/payments")
    public Flux<PaymentResponse> getAllPaymentByCreditId(@PathVariable String id) {
        return creditService.getAllPaysByCredirId(id);
    }
    @GetMapping("/client/{id}")
    public Flux<CreditResponse> getCreditByClientId(@PathVariable String id) {
        return creditService.getCreditByClientId(id);
    }
}
