package com.coffeeshop.product;

import com.coffeeshop.product.model.Product;
import com.coffeeshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner{

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        productRepository.save(new Product(null, "Arabica 1kg",
                "Delikatna kawa o owocowym aromacie", 45.00, "/images/arabica.jpg"));
        productRepository.save(new Product(null, "Robusta 1kg",
                "Mocna kawa o intensywnym smaku", 35.00, "/images/robusta.jpg"));
        productRepository.save(new Product(null, "Espresso 500g",
                "Kawa do ekspresu ciśnieniowego", 40.00, "/images/espresso.jpg"));
        productRepository.save(new Product(null, "Kawa bezkofeinowa 500g",
                "Łagodna kawa bez kofeiny", 38.00, "/images/decaf.jpg"));
        productRepository.save(new Product(null, "Ethiopia Yirgacheffe 250g",
                "Kawa specialty o cytrusowym aromacie", 65.00, "/images/ethiopia.jpg"));
    }
}
