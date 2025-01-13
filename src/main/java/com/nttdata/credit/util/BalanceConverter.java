package com.nttdata.credit.util;


import com.nttdata.credit.model.entity.Balance;
import com.nttdata.credit.model.entity.Credit;
import com.nttdata.credit.model.response.BalanceResponse;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceConverter {
    public static BalanceResponse toBalanceResponse(List<Credit> creditList) {
        BalanceResponse balanceResponse = new BalanceResponse();
        List<Balance> listBalances = creditList.stream()
                .map(creditCard -> new Balance(creditCard.getId(), creditCard.getOutstandingBalance(), new Date()))
                .collect(Collectors.toList());
        balanceResponse.setBalances(listBalances);
        if (!creditList.isEmpty()) {
            balanceResponse.setClientId(creditList.get(0).getClientId());
        }
        return balanceResponse;
    }

}
