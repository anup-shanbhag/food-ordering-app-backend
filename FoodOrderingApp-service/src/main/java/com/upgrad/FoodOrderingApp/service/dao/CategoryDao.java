package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryEntity> getAllCategories() {
        try {
            return entityManager.createNamedQuery("Category.fetchAllCategories", CategoryEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CategoryEntity getCategoryById(final String categoryId) {
        try {
            return entityManager.createNamedQuery("Category.fetchCategoryItem", CategoryEntity.class)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }
}
