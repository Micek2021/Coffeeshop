package com.coffeeshop.order.soap;

import com.coffeeshop.soap.PaymentRequest;
import com.coffeeshop.soap.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
@RequiredArgsConstructor
public class PaymentSoapClient {

    private final WebServiceTemplate webServiceTemplate;

    public PaymentResponse processPayment(PaymentRequest request) {
        return (PaymentResponse) webServiceTemplate.marshalSendAndReceive(request);
    }
}