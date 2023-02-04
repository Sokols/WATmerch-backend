package com.qsspy.watmerchbackend.repository;

import com.qsspy.watmerchbackend.entity.Category;
import com.qsspy.watmerchbackend.entity.Product;
import com.qsspy.watmerchbackend.entity.ProductBasicDetails;
import com.qsspy.watmerchbackend.entity.ProductDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void itShouldFindProductByCategoryIdAndNameContaining() {
        //given
        Category category = categoryRepository.save(new Category("TestCategory"));
        categoryRepository.save(category);
        Product product = new Product("1234", "Kubek", 2.34f, 0.2f);
        product.setDetails(new ProductDetails());
        product.setBasicDetails(new ProductBasicDetails());
        product.setCategory(category);
        final Product savedProduct = underTest.save(product);

        String searchWord = "Kub";

        //when
        Page<Product> retrievedProductPage = underTest.findByCategoryIdAndNameContaining(
                category.getId(), searchWord, PageRequest.of(0, 10));

        //then
        retrievedProductPage.getContent().forEach(
                dbProduct-> assertThat(dbProduct).isEqualTo(savedProduct));

        assertThat(retrievedProductPage.getContent().size()).isEqualTo(1);

    }

    @Test
    void itShouldFindProductByNameContaining() {
        //given
        Category category = categoryRepository.save(new Category("TestCategory"));
        categoryRepository.save(category);
        Product product = new Product("1234", "Kubek", 2.34f, 0.2f);
        product.setDetails(new ProductDetails());
        product.setBasicDetails(new ProductBasicDetails());
        product.setCategory(category);
        final Product savedProduct = underTest.save(product);

        String searchWord = "ek";

        //when
        Page<Product> retrievedProductPage = underTest.findByCategoryIdAndNameContaining(
                category.getId(), searchWord, PageRequest.of(0, 10));

        //then
        retrievedProductPage.getContent().forEach(
                dbProduct-> assertThat(dbProduct).isEqualTo(savedProduct));

        assertThat(retrievedProductPage.getContent().size()).isEqualTo(1);
    }

    @Test
    void itShouldNotFindProductByNameContaining() {
        //given
        Category category = categoryRepository.save(new Category("TestCategory"));
        categoryRepository.save(category);
        Product product = new Product("1234", "Kubek", 2.34f, 0.2f);
        product.setDetails(new ProductDetails());
        product.setBasicDetails(new ProductBasicDetails());
        product.setCategory(category);
        final Product savedProduct = underTest.save(product);

        String searchWord = "qweqwe";

        //when
        Page<Product> retrievedProductPage = underTest.findByCategoryIdAndNameContaining(
                category.getId(), searchWord, PageRequest.of(0, 10));

        //then
        retrievedProductPage.getContent().forEach(
                dbProduct-> assertThat(dbProduct).isNotEqualTo(savedProduct));

        assertThat(retrievedProductPage.getContent().size()).isEqualTo(0);
    }

    @Test
    void itShouldGetAllProductsBarcodes() {
        //given
        List<String> inputBarcodes = new ArrayList<>();

        Category category = categoryRepository.save(new Category("TestCategory"));
        categoryRepository.save(category);

        for(int i=0;i<10;i++) {

            inputBarcodes.add(String.valueOf(i));
            Product product = new Product(String.valueOf(i), String.valueOf(i), 2.34f, 0.2f);
            product.setDetails(new ProductDetails());
            product.setBasicDetails(new ProductBasicDetails());
            product.setCategory(category);
            underTest.save(product);
        }

        //when
        List<String> outputBarcodes = underTest.getBarcodes();

        //then
        assertThat(outputBarcodes).isEqualTo(inputBarcodes);
    }
}