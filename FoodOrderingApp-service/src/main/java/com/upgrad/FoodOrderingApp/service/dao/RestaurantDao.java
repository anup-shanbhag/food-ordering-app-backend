package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {
    @PersistenceContext
    EntityManager entityManager;

    public List<RestaurantEntity> restaurantsByRating(){
        return entityManager.createNamedQuery("Restaurants.fetchAll").getResultList();
    }

    public List<RestaurantEntity> restaurantsByName(String name){
        return entityManager.createNamedQuery("Restaurants.getByName").setParameter("name","%"+name.toLowerCase()+"%").getResultList();
    }

    public RestaurantEntity getRestaurantByID(String restaurantId){
        return entityManager.createNamedQuery("Restaurants.getById",RestaurantEntity.class).setParameter("id",restaurantId).getSingleResult();
    }

    public List<RestaurantEntity> restaurantByCategory(CategoryEntity categoryEntity){
        try {
            return entityManager.createNamedQuery("RestaurantCategoryEntity.getRestaurantByCategory",RestaurantEntity.class).setParameter("category", categoryEntity).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }
}