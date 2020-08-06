package com.upgrad.FoodOrderingApp.service.entity;

import com.upgrad.FoodOrderingApp.service.common.ItemType;
import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "item")
public class ItemEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemIdGenerator")
    @SequenceGenerator(name = "itemIdGenerator", sequenceName = "item_id_seq")
    @ToStringExclude
    @HashCodeExclude
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "item_name")
    @NotNull
    @Size(max = 30)
    private String itemName;

    @Column(name = "price")
    @NotNull
    private Integer price;

    @Column(name = "type")
    @Size(max = 10)
    @NotNull
    private ItemType type;

    @OneToMany(mappedBy = "itemEntity", cascade= CascadeType.ALL, fetch= FetchType.LAZY)
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private List<CategoryItemEntity> categoryItemEntityList;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    Set<OrderItemEntity> orders = new HashSet<OrderItemEntity>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public List<CategoryItemEntity> getCategoryItemEntityList() {
        return categoryItemEntityList;
    }

    public void setCategoryItemEntityList(List<CategoryItemEntity> categoryItemEntityList) {
        this.categoryItemEntityList = categoryItemEntityList;
    }

    public Set<OrderItemEntity> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderItemEntity> orders) {
        this.orders = orders;
    }

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
