package com.coffeeshop.product.grpc;

import com.coffeeshop.grpc.ProductRequest;
import com.coffeeshop.grpc.ProductResponse;
import com.coffeeshop.grpc.ProductServiceGrpc;
import com.coffeeshop.product.repository.ProductRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase{

    private final ProductRepository productRepository;

    @Override
    public void getProduct(ProductRequest request,
                           StreamObserver<ProductResponse> responseObserver) {
        productRepository.findById(request.getProductId())
                .ifPresentOrElse(
                        product -> {
                            ProductResponse response = ProductResponse.newBuilder()
                                    .setId(product.getId())
                                    .setName(product.getName())
                                    .setDescription(product.getDescription())
                                    .setPrice(product.getPrice())
                                    .setImageUrl(product.getImageUrl())
                                    .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        () -> responseObserver.onError(
                                Status.NOT_FOUND
                                        .withDescription("Product not found: " + request.getProductId())
                                        .asRuntimeException()
                        )
                );
    }
}
