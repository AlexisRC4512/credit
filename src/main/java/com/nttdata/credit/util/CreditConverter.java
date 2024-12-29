package com.nttdata.credit.util;

import com.nttdata.credit.model.entity.Credit;
import com.nttdata.credit.model.enums.TypeCredit;
import com.nttdata.credit.model.request.CreditRequest;
import com.nttdata.credit.model.response.CreditResponse;

public class CreditConverter {
    public static Credit toCredit(CreditRequest request) {
        Credit credit = new Credit();
        credit.setType(request.getType());
        credit.setAmount(request.getAmount());
        credit.setInterestRate(request.getInterestRate());
        credit.setStartDate(request.getStartDate());
        credit.setEndDate(request.getEndDate());
        credit.setOutstandingBalance(request.getOutstandingBalance());
        credit.setClientId(request.getClientId());
        credit.setPayments(request.getPayments());

        return credit;
    }

    public static CreditResponse toCreditResponse(Credit credit) {
        CreditResponse response = new CreditResponse();
        response.setId(credit.getId());
        response.setType(credit.getType());
        response.setAmount(credit.getAmount());
        response.setInterestRate(credit.getInterestRate());
        response.setStartDate(credit.getStartDate());
        response.setEndDate(credit.getEndDate());
        response.setOutstandingBalance(credit.getOutstandingBalance());
        response.setClientId(credit.getClientId());
        response.setPayments(credit.getPayments());
        return response;
    }
}
