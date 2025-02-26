package com.nttdata.credit.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    double amount;

    public void setAmount(double amount) {
        if (amount < 0 ) {
            throw new IllegalArgumentException("The amount it has to be may");
        }
        this.amount = amount;
    }
}
