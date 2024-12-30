package com.nttdata.credit.strategy;


import com.nttdata.credit.model.entity.Credit;
import com.nttdata.credit.model.enums.TypeClient;
import com.nttdata.credit.model.exception.CreditNotFoundException;
import com.nttdata.credit.model.response.CreditResponse;
import com.nttdata.credit.repository.CreditRespository;
import com.nttdata.credit.util.CreditConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.Map;
import java.util.function.Function;

@Component
public class ValidationStrategy {

    private final CreditRespository creditRespository;

    public ValidationStrategy(CreditRespository creditRespository) {
        this.creditRespository = creditRespository;
    }

    public final Map<String, Function<Credit, Mono<CreditResponse>>> validationStrategies = Map.of(
            TypeClient.PERSONAL.name(), this::validatePersonalClient,
            TypeClient.BUSINESS.name(), this::validateBusinessClient
    );

    private Mono<CreditResponse> validatePersonalClient(Credit credit) {
        return creditRespository.findByClientId(credit.getClientId())
                .collectList()
                .flatMap(existingCredits -> {
                    if (existingCredits.size() >= 1) {
                        return Mono.error(new CreditNotFoundException("Personal client can only have one credit"));
                    }
                    return saveCredit(credit);
                });
    }

    private Mono<CreditResponse> validateBusinessClient(Credit credit) {
        return saveCredit(credit);
    }

    private Mono<CreditResponse> saveCredit(Credit credit) {
        return creditRespository.save(credit)
                .map(CreditConverter::toCreditResponse);
    }
}
