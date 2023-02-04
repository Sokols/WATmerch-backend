package com.qsspy.watmerchbackend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "purchase")
@NoArgsConstructor
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "is_finished")
    private boolean isFinished;

    @Column(name = "is_paid")
    private boolean isPaid;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private Address shippingAddress;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id")
    private Address billingAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ShopUser user;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private List<OrderProduct> orderProducts;

    public Purchase(Date purchaseDate, boolean isFinished, boolean isPaid) {
        this.purchaseDate = purchaseDate;
        this.isFinished = isFinished;
        this.isPaid = isPaid;
    }
}
