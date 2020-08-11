package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private OrderDao orderDao;

    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUuid, String categoryUuid) {
        RestaurantEntity restaurant = restaurantDao.getRestaurantByID(restaurantUuid);
        CategoryEntity category = categoryDao.getCategoryById(categoryUuid);
        return itemDao.getItemsByCategoryAndRestaurant(restaurant, category);
    }

    public List<ItemEntity> getItemsByCategory(String categoryUuid) {
        CategoryEntity category = categoryDao.getCategoryById(categoryUuid);
        return itemDao.getItemsByCategory(category);
    }


    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) throws RestaurantNotFoundException {

        /* Get All items orders in a particular restaurant  */
        List<ItemEntity> itemEntityList = new ArrayList<>();
        for (OrderEntity orderEntity : orderDao.getOrdersByRestaurant(restaurantEntity)) {
            orderEntity.getItems().forEach(items ->
                    itemEntityList.add(items.getItem())
            );
        }

        /* Create unsorted map of items orders by count */
        Map<String, Integer> unsortedItemCountMap = new HashMap<>();
        for (ItemEntity itemEntity : itemEntityList) {
            Integer count = unsortedItemCountMap.get(itemEntity.getUuid());
            unsortedItemCountMap.put(itemEntity.getUuid(), (count == null) ? 1 : count + 1);
        }

        /* Convert unsorted HashMap to list for sorting */
        List<Map.Entry<String, Integer> > unsortedItemCountList =
                new LinkedList<Map.Entry<String, Integer> >(unsortedItemCountMap.entrySet());

        /* Sort the list */
        unsortedItemCountList.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> item1,
                               Map.Entry<String, Integer> item2) {
                return (item2.getValue()).compareTo(item1.getValue());
            }
        });

        List<ItemEntity> sortedItemEntityList = new ArrayList<>();
        unsortedItemCountList.forEach(list ->
                sortedItemEntityList.add(itemDao.getItemByUUID(list.getKey()))
        );

        return sortedItemEntityList;
    }
}
