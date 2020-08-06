package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao{
    @PersistenceContext
    EntityManager entityManager;

    public List<ItemEntity> getItemsByCategoryAndRestaurant(RestaurantEntity restaurant, CategoryEntity category){
       return entityManager.createNamedQuery("CategoryItemEntity.getItemByRestaurantAndCategory",ItemEntity.class)
            .setParameter("restaurant",restaurant)
            .setParameter("category",category)
            .getResultList();
    }

    public List<ItemEntity> getItemsByCategory(CategoryEntity category){
        return entityManager.createNamedQuery("CategoryItemEntity.getItemByRestaurantAndCategory",ItemEntity.class)
            .setParameter("category",category)
            .getResultList();
    }
}
