package com.nttdata.credit.testService;

import com.nttdata.credit.model.entity.Balance;
import com.nttdata.credit.model.entity.Credit;
import com.nttdata.credit.model.entity.Payment;
import com.nttdata.credit.model.enums.TypeCredit;
import com.nttdata.credit.model.request.CreditRequest;
import com.nttdata.credit.model.response.BalanceResponse;
import com.nttdata.credit.model.response.CreditResponse;
import com.nttdata.credit.model.response.PaymentResponse;
import com.nttdata.credit.repository.CreditRespository;
import com.nttdata.credit.service.impl.CreditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestService {
    @Mock
    private CreditRespository creditRepository;
    @InjectMocks
    private CreditServiceImpl creditService;
    private Credit credit;
    private CreditRequest creditRequest;

    @BeforeEach
    void setUp() {
        credit = new Credit();
        credit.setId("1");
        credit.setType(TypeCredit.PERSONAL);

        creditRequest = new CreditRequest();
        creditRequest.setType(TypeCredit.PERSONAL);
    }

    @Test
    void getAllCreditsSuccess() {
        when(creditRepository.findAll()).thenReturn(Flux.just(credit));

        StepVerifier.create(creditService.getAllCredits())
                .expectNextMatches(creditResponse -> creditResponse != null)
                .verifyComplete();

        verify(creditRepository, times(1)).findAll();
    }

    @Test
    void getAllCreditsError() {
        when(creditRepository.findAll()).thenReturn(Flux.error(new RuntimeException("Error")));

        StepVerifier.create(creditService.getAllCredits())
                .expectErrorMatches(throwable -> throwable instanceof Exception &&
                        throwable.getMessage().equals("Error fetching all Credits"))
                .verify();

        verify(creditRepository, times(1)).findAll();
    }

    @Test
    void getCreditByIdSuccess() {
        when(creditRepository.findById("1")).thenReturn(Mono.just(credit));

        StepVerifier.create(creditService.getCreditById("1"))
                .expectNextMatches(creditResponse -> creditResponse != null)
                .verifyComplete();

        verify(creditRepository, times(1)).findById("1");
    }

    @Test
    void getCreditByIdError() {
        when(creditRepository.findById("1")).thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(creditService.getCreditById("1"))
                .expectErrorMatches(throwable -> throwable instanceof Exception &&
                        throwable.getMessage().equals("Error fetching Credit by id"))
                .verify();

        verify(creditRepository, times(1)).findById("1");
    }


    @Test
    void updateCreditSuccess() {
        when(creditRepository.findById("1")).thenReturn(Mono.just(credit));
        when(creditRepository.save(any(Credit.class))).thenReturn(Mono.just(credit));

        StepVerifier.create(creditService.updateCredit("1", creditRequest))
                .expectNextMatches(creditResponse -> creditResponse != null)
                .verifyComplete();

        verify(creditRepository, times(1)).findById("1");
        verify(creditRepository, times(1)).save(any(Credit.class));
    }

    @Test
    void updateCreditError() {
        when(creditRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(creditService.updateCredit("1", creditRequest))
                .expectErrorMatches(throwable -> throwable instanceof Exception &&
                        throwable.getMessage().equals("Error updating Credit"))
                .verify();

        verify(creditRepository, times(1)).findById("1");
    }

    @Test
    void deleteCreditSuccess() {
        when(creditRepository.findById("1")).thenReturn(Mono.just(credit));
        when(creditRepository.delete(credit)).thenReturn(Mono.empty());

        StepVerifier.create(creditService.deleteCredit("1"))
                .verifyComplete();

        verify(creditRepository, times(1)).findById("1");
        verify(creditRepository, times(1)).delete(credit);
    }

    @Test
    void deleteCreditError() {
        when(creditRepository.findById("1")).thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(creditService.deleteCredit("1"))
                .expectErrorMatches(throwable -> throwable instanceof Exception &&
                        throwable.getMessage().equals("Error deleting Credit"))
                .verify();

        verify(creditRepository, times(1)).findById("1");
    }
    @Test
    public void testGetBalanceByClientIdSuccess() {
        String idClient = "clientId";

        Credit credit = new Credit();
        credit.setClientId(idClient);
        credit.setOutstandingBalance(100);

        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setClientId(idClient);
        balanceResponse.setBalances(Collections.singletonList(new Balance(idClient, 100, new Date())));

        when(creditRepository.findByClientId(idClient)).thenReturn(Flux.just(credit));

        Flux<BalanceResponse> result = creditService.getBalanceByClientId(idClient);

        StepVerifier.create(result)
                .expectNextMatches(response -> response != null && response.getClientId().equals(idClient))
                .verifyComplete();
    }

    @Test
    public void testGetBalanceByClientIdCreditNotFound() {
        String idClient = "clientId";

        when(creditRepository.findByClientId(idClient)).thenReturn(Flux.empty());

        Flux<BalanceResponse> result = creditService.getBalanceByClientId(idClient);

        StepVerifier.create(result)
                .expectError(Exception.class)
                .verify();
    }
    @Test
    public void testGetCreditByClientIdSuccess() {
        String idClient = "clientId";

        Credit credit = new Credit();
        credit.setClientId(idClient);

        CreditResponse creditResponse = new CreditResponse();
        creditResponse.setClientId(idClient);

        when(creditRepository.findByClientId(idClient)).thenReturn(Flux.just(credit));

        Flux<CreditResponse> result = creditService.getCreditByClientId(idClient);

        StepVerifier.create(result)
                .expectNextMatches(response -> response != null && response.getClientId().equals(idClient))
                .verifyComplete();
    }

    @Test
    public void testGetCreditByClientIdCreditNotFound() {
        String idClient = "clientId";

        when(creditRepository.findByClientId(idClient)).thenReturn(Flux.empty());

        Flux<CreditResponse> result = creditService.getCreditByClientId(idClient);

        StepVerifier.create(result)
                .expectError(Exception.class)
                .verify();
    }
    @Test
    void testGetAllPaysByCredirIdSuccess() {
        String creditId = "123";
        Payment payment1 = new Payment(100, new Date(), "Payment 1");
        Payment payment2 = new Payment(200, new Date(), "Payment 2");
        Credit credit = new Credit();
        credit.setPayments(Arrays.asList(payment1, payment2));

        when(creditRepository.findById(creditId)).thenReturn(Mono.just(credit));

        Flux<PaymentResponse> result = creditService.getAllPaysByCredirId(creditId);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getAmount() == 100)
                .expectNextMatches(response -> response.getAmount() == 200)
                .verifyComplete();

        verify(creditRepository).findById(creditId);
    }
}
