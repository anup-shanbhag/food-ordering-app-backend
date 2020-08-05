package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {
    @PersistenceContext
    EntityManager entityManager;

    public CouponEntity getCouponByCouponName(final String couponName) {
        try {
            return entityManager.createNamedQuery("Coupon.ByName", CouponEntity.class)
                    .setParameter("couponName", couponName)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
