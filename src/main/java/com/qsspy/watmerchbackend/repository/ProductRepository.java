package com.qsspy.watmerchbackend.repository;

import com.qsspy.watmerchbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByCategoryIdAndNameContaining(int categoryId, String partString, Pageable pageable);
    Page<Product> findByNameContaining(String partString, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT product.barcode FROM product")
    List<String> getBarcodes();
}
