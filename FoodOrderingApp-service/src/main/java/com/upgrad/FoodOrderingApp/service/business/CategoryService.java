package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.*;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        return categoryDao.getAllCategories();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CategoryEntity getCategoryById(String categoryId) throws CategoryNotFoundException {

        if(categoryId.equals("")){
            throw new CategoryNotFoundException(CNF_001.getCode(), CNF_001.getDefaultMessage());
        }

        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryId);

        if(categoryEntity==null){
            throw new CategoryNotFoundException(CNF_002.getCode(), CNF_002.getDefaultMessage());
        }

        return categoryEntity;
    }

}
