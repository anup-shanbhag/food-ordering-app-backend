package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.CNF_001;
import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.RNF_003;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    public List<RestaurantEntity> restaurantsByRating(){
        return restaurantDao.restaurantsByRating();
    }

    public List<RestaurantEntity> restaurantsByName(String name) throws RestaurantNotFoundException {
        if (name.trim().length() <= 0)
            throw new RestaurantNotFoundException(RNF_003.getCode(), RNF_003.getDefaultMessage());
        return restaurantDao.restaurantsByName(name);
    }

    public List<RestaurantEntity> restaurantByCategory(String categoryUuid) throws CategoryNotFoundException {
        if(categoryUuid.trim().length() <= 0){
            throw new CategoryNotFoundException(CNF_001.getCode(),CNF_001.getDefaultMessage());
        }
        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryUuid);
        return restaurantDao.restaurantByCategory(categoryEntity);
    }

}
