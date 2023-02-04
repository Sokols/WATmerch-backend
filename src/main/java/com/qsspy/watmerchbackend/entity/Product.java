package com.qsspy.watmerchbackend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "barcode")
    private String barcode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "VAT")
    private float vat;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "basic_details_id",referencedColumnName = "id", nullable = false)
    private ProductBasicDetails basicDetails;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "details_id",referencedColumnName = "id", nullable = false)
    private ProductDetails details;

    public Product(String barcode, String name, float price, float vat) {
        this.barcode = barcode;
        this.name = name;
        this.price = price;
        this.vat = vat;
    }
}
