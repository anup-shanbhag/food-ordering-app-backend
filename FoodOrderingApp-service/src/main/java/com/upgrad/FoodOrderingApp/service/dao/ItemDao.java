package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
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

    public List<ItemEntity> getItemsByCategory(CategoryEntity category){
        return entityManager.createNamedQuery("CategoryItemEntity.getItemByCategory",ItemEntity.class)
            .setParameter("category",category)
            .getResultList();
    }


    public ItemEntity getItemById(String uuid){
        try {
            return entityManager.createNamedQuery("ItemEntity.getItemById", ItemEntity.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }
}
