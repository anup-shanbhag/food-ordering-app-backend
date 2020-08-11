package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao{
    @PersistenceContext
    EntityManager entityManager;

    public List<ItemEntity> getItemsByCategoryAndRestaurant(RestaurantEntity restaurant, CategoryEntity category){
        try {
            return entityManager.createNamedQuery("CategoryItemEntity.getItemByRestaurantAndCategory", ItemEntity.class)
                    .setParameter("restaurant", restaurant)
                    .setParameter("category", category)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    public List<ItemEntity> getItemsByCategory(CategoryEntity category){
        try {
            return entityManager.createNamedQuery("CategoryItemEntity.getItemByRestaurantAndCategory", ItemEntity.class)
                    .setParameter("category", category)
                    .getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    public ItemEntity getItemByUUID(String uuid) {
        try {
            return entityManager.createNamedQuery("fetchItemByID", ItemEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
