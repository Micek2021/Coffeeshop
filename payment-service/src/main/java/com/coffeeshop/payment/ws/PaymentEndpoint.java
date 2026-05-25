package com.coffeeshop.payment.ws;

import com.coffeeshop.soap.PaymentRequest;
import com.coffeeshop.soap.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Random;

@Endpoint
@Slf4j
public class PaymentEndpoint {
    private static final String NAMESPACE_URI = "http://coffeeshop.com/payment";
    private final Random random = new Random();

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "PaymentRequest")
    @ResponsePayload
    public PaymentResponse processPayment(@RequestPayload PaymentRequest request){
        double price = request.getTotalPrice();
        boolean paymentStatus;
        long id = request.getOrderId();

        log.info("Payment requested for order: {}", id);
        log.info("Total price to process: {}", price);

        PaymentResponse response = new PaymentResponse();


        if (price >= 500.0){
            log.warn("Payment rejected: Amount {} exceeds the 500 limit", price);
            paymentStatus = false;
        } else {
            int chance = random.nextInt(100);

            if (chance < 95){
                log.info("Payment approved for order: {}", id);
                paymentStatus = true;
            } else {
                log.info("Payment rejected: 5% random failure triggered for order: {}", id);
                paymentStatus = false;
            }
        }

        response.setOrderId(id);
        response.setPaymentApproved(paymentStatus);
        return response;
    }
}
