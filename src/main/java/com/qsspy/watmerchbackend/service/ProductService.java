package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.Category;
import com.qsspy.watmerchbackend.entity.Product;
import com.qsspy.watmerchbackend.repository.CategoryRepository;
import com.qsspy.watmerchbackend.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class ProductService implements IProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public Category postCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProducts(int page, int size, Integer categoryId, Boolean extended, Boolean detailed, String namePart) {

        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Product> productPage;
        if(categoryId == null) {
            productPage = productRepository.findByNameContaining(namePart, pageable);
        } else {
            productPage = productRepository.findByCategoryIdAndNameContaining(categoryId, namePart, pageable);
        }
        for(Product product : productPage.getContent()) {
            if(!extended) {
                product.setBasicDetails(null);
            }
            if(!detailed) {
                product.setDetails(null);
            }
        }
        return productPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(String barcode) {
        return productRepository.findById(barcode).get();
    }

    @Override
    public Product postProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getRandomProducts(int productCount, Boolean extended, Boolean detailed) {

        if(productCount<1) {
            throw new IllegalArgumentException("Product count can't be less than 1");
        }

        List<String> barcodes = productRepository.getBarcodes();

        List<String> randomBarcodes = new ArrayList<>();

        if(barcodes.size() <= productCount) {
            randomBarcodes = barcodes;
        } else {
            Random rand = new Random();

            for(int i=0;i<productCount;i++) {
                randomBarcodes.add(barcodes.remove(rand.nextInt(barcodes.size())));
            }
        }

        List<Product> products = new ArrayList<>();
        for(String barcode : randomBarcodes) {
            Product product = productRepository.findById(barcode).get();

            if(!extended) {
                product.setBasicDetails(null);
            }
            if(!detailed) {
                product.setDetails(null);
            }

            products.add(product);
        }

        return products;
    }
}
