package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
@Service
public class RestaurantService {
    @Autowired
    private RestaurantDao restaurantDao;

    public List<RestaurantEntity> restaurantsByRating(){
        return restaurantDao.restaurantsByRating();
    }

    public List<RestaurantEntity> restaurantsByName(String name){
        return restaurantDao.restaurantsByName(name);
    }

}
