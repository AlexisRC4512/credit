package com.nttdata.credit.util;

import com.nttdata.credit.model.entity.Payment;
import com.nttdata.credit.model.response.PaymentResponse;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

public class PaymentConverter {
    public static PaymentResponse toPaymentResponse(Payment payment) {
        PaymentResponse paymentResponse = new PaymentResponse(payment.getAmount(), new Date());
        return paymentResponse;
    }
    public static Flux<PaymentResponse> toListPaymentResponse(List<PaymentResponse>paymentResponseList) {
        Flux<PaymentResponse>newPaymentResponseList = Flux.just((PaymentResponse) paymentResponseList);
        return newPaymentResponseList;
    }
}
