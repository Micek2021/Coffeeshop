package com.coffeeshop.order.grpc;

import com.coffeeshop.grpc.ProductRequest;
import com.coffeeshop.grpc.ProductResponse;
import com.coffeeshop.grpc.ProductServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductGrpcClient {

    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

    public ProductResponse getProduct(Long productId){
        try {
            ProductRequest request = ProductRequest.newBuilder()
                    .setProductId(productId)
                    .build();
            return productServiceStub.getProduct(request);
        } catch (StatusRuntimeException e) {
            log.error("gRPC call failed for productId {}: {}", productId, e.getStatus());
            throw new RuntimeException("Product not found: " + productId);
        }
    }
}
