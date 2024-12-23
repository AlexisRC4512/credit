package com.nttdata.credit.model.response;

import com.nttdata.credit.model.entity.Balance;
import com.nttdata.credit.model.entity.Payment;
import com.nttdata.credit.model.entity.Transaction;
import com.nttdata.credit.model.enums.TypeCredit;
import lombok.*;

import java.util.Date;
import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditResponse {
    private String id;
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
}
