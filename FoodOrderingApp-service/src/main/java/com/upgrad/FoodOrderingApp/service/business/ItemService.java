package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ItemService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

  public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUuid, String categoryUuid){
      RestaurantEntity restaurant = restaurantDao.getRestaurantByID(restaurantUuid);
      CategoryEntity category = categoryDao.getCategoryById(categoryUuid);
        return itemDao.getItemsByCategoryAndRestaurant(restaurant,category);
  }

    public List<ItemEntity> getItemsByCategory(String categoryUuid){
        CategoryEntity category = categoryDao.getCategoryById(categoryUuid);
        return itemDao.getItemsByCategory(category);
    }
}
