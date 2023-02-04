package com.qsspy.watmerchbackend.controller.rest;

import com.qsspy.watmerchbackend.entity.Category;
import com.qsspy.watmerchbackend.entity.Product;
import com.qsspy.watmerchbackend.service.IProductService;
import com.qsspy.watmerchbackend.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final IProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Pobiera listę kategorii", notes = "Pobiera listę kategorii. Nie wymaga autoryzacji")
    @GetMapping("/categories")
    public List<Category> getCategories() {

        return productService.getCategories();
    }

    @ApiOperation(value = "Zapisuje nową kategorię", notes = "Zapisuje nową kategorię. Dostęp ma tylko Pracownik")
    @PostMapping("/categories")
    public Category postCategory(
            @ApiParam(name = "Nowa Kategoria") @RequestBody Category category)
    {
        return productService.postCategory(category);
    }

    //Pobiera listę produktów
    @ApiOperation(value = "Pobiera stronę listy produktów", notes = "Pobiera stronę listy produktów. Nie wymaga autoryzacji")
    @GetMapping("/products")
    public Page<Product> getProducts(
            @ApiParam(name = "Numer strony (0-indexed)") @RequestParam(defaultValue = "0") int page, // numer strony (0-indexed
            @ApiParam(name = "Wielkość strony") @RequestParam(defaultValue = "20") int size, // wielkość strony
            @ApiParam(name = "Id kategorii") @RequestParam(name = "category", required = false) Integer categoryId, //id kategorii
            @ApiParam(name = "Załączenie dodatkowych danych") @RequestParam(defaultValue = "false") Boolean extended,
            @ApiParam(name = "Załączenie szczegółowych danych") @RequestParam(defaultValue = "false") Boolean detailed,
            @ApiParam(name = "Słowo wyszukiwania") @RequestParam(name = "contains", defaultValue = "") String namePart
    ) {
        return productService.getProducts(page, size, categoryId, extended, detailed, namePart);
    }

    @ApiOperation(value = "Pobranie danych pojedyńczego produktu",
                  notes = "Pobranie danych pojedyńczego produktu po kodzie kreskowym")
    @GetMapping("/products/{barcode}")
    public Product getProduct(
            @PathVariable String barcode
    ) {
        return productService.getProduct(barcode);
    }

    @ApiOperation(value = "Dodanie nowego produktu",
                  notes = "Dodanie nowego produktu. Dostęp tylko dla pracownika. Pole 'barcode' musi zawierać konkretny kod kreskowy")
    @PostMapping("/products")
    public Product postProduct(@RequestBody Product product) {

        return productService.postProduct(product);
    }

    @GetMapping("products/randomProducts")
    public List<Product> getRandomProducts(
            @RequestParam(defaultValue = "false") Boolean extended,
            @RequestParam(defaultValue = "false") Boolean detailed,
            @RequestParam(defaultValue = "5") int count
    ) {
        return productService.getRandomProducts(count,extended,detailed);
    }
}
