package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderIdGenerator")
    @SequenceGenerator(name = "orderIdGenerator", sequenceName = "order_id_seq")
    @ToStringExclude
    @HashCodeExclude
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private Integer uuid;

    @Column(name = "bill")
    @NotNull
    private String bill;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id",referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private CouponEntity coupon;

    @Column(name = "discount")
    private String discount;

    @Column(name = "date")
    @NotNull
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id",referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private PaymentEntity payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id",referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private AddressEntity address;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id",referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    Set<OrderItemEntity> items = new HashSet<OrderItemEntity>();

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this,obj,Boolean.FALSE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,Boolean.FALSE);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
