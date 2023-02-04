package com.qsspy.watmerchbackend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "product_basic_details")
@Data
public class ProductBasicDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "logo_image")
    @Lob
    private byte[] logoImage;

    @Column(name = "discount_percent")
    private float discountPercent;
}
