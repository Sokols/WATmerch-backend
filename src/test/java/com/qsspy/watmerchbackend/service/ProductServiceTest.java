package com.qsspy.watmerchbackend.service;

import com.qsspy.watmerchbackend.entity.Category;
import com.qsspy.watmerchbackend.entity.Product;
import com.qsspy.watmerchbackend.entity.ProductBasicDetails;
import com.qsspy.watmerchbackend.entity.ProductDetails;
import com.qsspy.watmerchbackend.repository.CategoryRepository;
import com.qsspy.watmerchbackend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private IProductService underTest;

    @BeforeEach
    void setUp() {

        this.underTest = new ProductService(categoryRepository, productRepository);
    }

    @Test
    void itShouldGetAllCategories() {
        //given
        //when
        underTest.getCategories();
        //then
        verify(categoryRepository).findAll();
    }

    @Test
    void itShouldPostCategory() {
        //given
        Category inputCategory = new Category("Koszulki");
        //when
        underTest.postCategory(inputCategory);
        //then
        ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(inputCategory);
    }

    @Test
    void itShouldGetProductsWithAnyCategory() {
        //given
        int page = 0;
        int size = 10;
        Integer categoryId = null;
        boolean extended = true;
        boolean detailed = false;
        String namePart = "";
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = Mockito.mock(Page.class);

        List<Product> testProducts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Product product = new Product(String.valueOf(i), "P" + i, 20.0f + i, 0.22f);
            product.setBasicDetails(new ProductBasicDetails());
            product.setDetails(new ProductDetails());
            testProducts.add(product);
        }

        given(productPage.getContent()).willReturn(testProducts);
        given(productRepository.findByNameContaining(namePart, pageable)).willReturn(productPage);
        //when
        underTest.getProducts(page, size, categoryId, extended, detailed, namePart);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(productRepository).findByNameContaining(stringArgumentCaptor.capture(), pageableArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(namePart);
        assertThat(pageableArgumentCaptor.getValue()).isEqualTo(pageable);
    }

    @Test
    void itShouldGetProductsWithSpecifiedCategory() {
        //given
        int page = 0;
        int size = 10;
        Integer categoryId = 1;
        boolean extended = false;
        boolean detailed = true;
        String namePart = "";
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = Mockito.mock(Page.class);

        List<Product> testProducts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Product product = new Product(String.valueOf(i), "P" + i, 20.0f + i, 0.22f);
            product.setBasicDetails(new ProductBasicDetails());
            product.setDetails(new ProductDetails());
            testProducts.add(product);
        }

        given(productPage.getContent()).willReturn(testProducts);
        given(productRepository.findByCategoryIdAndNameContaining(categoryId, namePart, pageable)).willReturn(productPage);
        //when
        underTest.getProducts(page, size, categoryId, extended, detailed, namePart);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(productRepository).findByCategoryIdAndNameContaining(
                integerArgumentCaptor.capture(), stringArgumentCaptor.capture(), pageableArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getValue()).isEqualTo(namePart);
        assertThat(pageableArgumentCaptor.getValue()).isEqualTo(pageable);
        assertThat(integerArgumentCaptor.getValue()).isEqualTo(categoryId);
    }

    @Test
    void itShouldGetProductByBarcode() {
        //given
        String barcode = "123";
        Product product = new Product(barcode, "TestProduct", 20.0f, 0.23f);
        given(productRepository.findById(anyString())).willReturn(Optional.of(product));
        //when
        Product outputProduct = underTest.getProduct(barcode);
        //then
        verify(productRepository).findById(anyString());
        assertThat(outputProduct).isEqualTo(product);
    }

    @Test
    void itShouldPostProduct() {
        //given
        String barcode = "123";
        Product product = new Product(barcode, "TestProduct", 20.0f, 0.23f);
        given(productRepository.save(product)).willReturn(product);
        //when
        Product outputProduct = underTest.postProduct(product);
        //then
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(argumentCaptor.capture());
        assertThat(outputProduct).isEqualTo(product);
        assertThat(argumentCaptor.getValue()).isEqualTo(product);
    }

    @Test
    void itShouldGetRandomProductsAsAllProducts() {

        int productCount = 10;
        boolean extended = false;
        boolean detailed = true;
        Product testProduct = new Product("12", "P12", 20.22f, 0.22f);
        testProduct.setDetails(new ProductDetails());
        testProduct.setBasicDetails(new ProductBasicDetails());
        List<String> barcodes = Arrays.asList("10", "11", "12");

        given(productRepository.getBarcodes()).willReturn(barcodes);
        given(productRepository.findById(anyString())).willReturn(Optional.of(testProduct));
        //when
        List<Product> outputProducts = underTest.getRandomProducts(productCount, extended, detailed);
        //then
        assertThat(outputProducts).isEqualTo(barcodes.stream()
                .map(barcode -> testProduct)
                .collect(Collectors.toList()));
    }

    @Test
    void itShouldGetRandomProducts() {

        int productCount = 2;
        boolean extended = true;
        boolean detailed = false;
        Product testProduct = new Product("12", "P12", 20.22f, 0.22f);
        testProduct.setDetails(new ProductDetails());
        testProduct.setBasicDetails(new ProductBasicDetails());
        List<String> barcodes = new ArrayList<>(Arrays.asList("10", "11", "12"));


        given(productRepository.getBarcodes()).willReturn(barcodes);
        given(productRepository.findById(anyString())).willReturn(Optional.of(testProduct));
        //when
        List<Product> outputProducts = underTest.getRandomProducts(productCount, extended, detailed);
        //then
        outputProducts.forEach(product -> {
            assertThat(product).isIn(barcodes.stream().map(barcode->testProduct).collect(Collectors.toList()));
        });
        assertThat(outputProducts.size()).isEqualTo(productCount);
    }

    @Test
    void itShouldThrowExceptionIfProductsCountIsLesserThanOne() {

        int productCount = 0;
        boolean extended = true;
        boolean detailed = false;
        Product testProduct = new Product("12", "P12", 20.22f, 0.22f);
        testProduct.setDetails(new ProductDetails());
        testProduct.setBasicDetails(new ProductBasicDetails());

        //when
        //then
        assertThatThrownBy(()->underTest.getRandomProducts(productCount, extended, detailed))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product count can't be less than 1");
    }
}