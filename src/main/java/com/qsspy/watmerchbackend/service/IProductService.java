package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.Category;
import com.qsspy.watmerchbackend.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {

    List<Category> getCategories();
    Category postCategory(Category category);

    Page<Product> getProducts(int page, int size, Integer categoryId, Boolean extended, Boolean detailed, String namePart);
    Product getProduct(String barcode);
    Product postProduct(Product product);

    List<Product> getRandomProducts(int productCount, Boolean extended, Boolean detailed);

}
