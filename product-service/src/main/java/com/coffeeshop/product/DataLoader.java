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
                "Delikatna kawa o owocowym aromacie", 45.00, "https://www.ekspresydokawy.pl/public/upload/sellasist_cache/thumb_xlarge_73e5c5527731035b117279d4cd07dd68.jpg"));
        productRepository.save(new Product(null, "Robusta 1kg",
                "Mocna kawa o intensywnym smaku", 35.00, "https://skimacoffee.pl/wp-content/uploads/2025/05/robusta_seria_5_2560x2560px-1.jpg"));
        productRepository.save(new Product(null, "Espresso 500g",
                "Kawa do ekspresu ciśnieniowego", 40.00, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWRIj4bzQSquio1zsVbzWnGhzzxW4FZ5-R1Q&s"));
        productRepository.save(new Product(null, "Kawa bezkofeinowa 500g",
                "Łagodna kawa bez kofeiny", 38.00, "https://kawapartner.pl/16276-large_default/bezkofeinowa-lavazza-caffe-decaffeinato-500g.jpg"));
        productRepository.save(new Product(null, "Ethiopia Yirgacheffe 250g",
                "Kawa specialty o cytrusowym aromacie", 65.00, "https://kawepale.pl/wp-content/uploads/2022/09/ethiopia_yirgacheffee_przelew_front_fullprint.png"));
    }
}
