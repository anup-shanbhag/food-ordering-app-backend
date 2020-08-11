package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<OrderEntity> getOrdersByCustomers(final String uuid) {
        return entityManager.createNamedQuery("Orders.ByCustomer", OrderEntity.class).setParameter("customerId",uuid).getResultList();
    }

    public OrderEntity saveOrder(OrderEntity orderEntity){
         entityManager.merge(orderEntity);
         return orderEntity;
    }


    public OrderItemEntity saveOrderItem(OrderItemEntity orderedItem) {
        entityManager.merge(orderedItem);
        return orderedItem;
    }

    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurant) {
        try {
            return entityManager.createNamedQuery("fetchOrdersByRestaurant", OrderEntity.class)
                    .setParameter("restaurant", restaurant).getResultList();
        } catch (NoResultException nre) {
System.out.printf("Ashik0 ");
            return null;
        }
    }
}
