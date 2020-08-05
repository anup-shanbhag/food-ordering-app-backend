package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        System.out.println("Name: " + name );
        return entityManager.createNamedQuery("Restaurants.getByName").setParameter("name","%"+name.toLowerCase()+"%").getResultList();
    }
}
