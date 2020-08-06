package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;

@Entity
@Table(name="category_item")
@NamedQueries({
    @NamedQuery(name="CategoryItemEntity.getItemByRestaurantAndCategory",query = "SELECT ci.itemEntity FROM CategoryItemEntity ci WHERE ci.categoryEntity=:category AND ci.itemEntity=:restaurant"),
    @NamedQuery(name="CategoryItemEntity.getItemByCategory",query = "SELECT ci.itemEntity FROM CategoryItemEntity ci WHERE ci.categoryEntity=:category")

})
public class CategoryItemEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="CategoryItemEntityIdGenerator")
    @SequenceGenerator(name="CategoryItemEntityIdGenerator", sequenceName = "category_item_id_seq")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private CategoryEntity categoryEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ToStringExclude
    @HashCodeExclude
    @EqualsExclude
    private ItemEntity itemEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }
}
