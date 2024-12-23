package com.nttdata.credit.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nttdata.credit.model.entity.Balance;
import com.nttdata.credit.model.entity.Payment;
import com.nttdata.credit.model.entity.Transaction;
import com.nttdata.credit.model.enums.TypeCredit;
import com.nttdata.credit.util.CreditTypeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreditRequest {
    @JsonDeserialize(using = CreditTypeDeserializer.class)
    private TypeCredit type;
    private double amount;
    private double interestRate;
    private Date startDate;
    private Date endDate;
    private double outstandingBalance;
    private String clientId;
    private List<Payment> payments;
    private List<Transaction> transactions;
    private Balance balances;

    public CreditRequest(TypeCredit type, double amount, double interestRate, Date startDate, Date endDate, double outstandingBalance, String clientId
    ,List<Payment> payments,List<Transaction> transactions,Balance balances) {
        setType(type);
        setAmount(amount);
        setInterestRate(interestRate);
        setStartDate(startDate);
        setEndDate(endDate);
        setOutstandingBalance(outstandingBalance);
        setClientId(clientId);
        setPayments(payments);
        setTransactions(transactions);
        setBalances(balances);
    }

    public void setType(TypeCredit type) {
        if (type == null ) {
            throw new IllegalArgumentException("Type must be either 'personal' or 'business'");
        }
        this.type = type;
    }

    public void setAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        this.amount = amount;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate < 0) {
            throw new IllegalArgumentException("Interest rate must be non-negative");
        }
        this.interestRate = interestRate;
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date is required");
        }
        this.endDate = endDate;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        if (outstandingBalance < 0) {
            throw new IllegalArgumentException("Outstanding balance must be non-negative");
        }
        this.outstandingBalance = outstandingBalance;
    }

    public void setClientId(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Client ID is required");
        }
        this.clientId = clientId;
    }
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setBalances(Balance balances) {
        this.balances = balances;
    }
}
