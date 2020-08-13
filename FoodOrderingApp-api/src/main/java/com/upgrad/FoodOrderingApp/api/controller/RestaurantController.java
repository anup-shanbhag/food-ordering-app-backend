package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.business.ItemService;
import com.upgrad.FoodOrderingApp.service.business.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.AppUtils;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.*;



@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CustomerService customerService;


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurantDetails() {
        List<RestaurantList> restaurantList = new ArrayList<RestaurantList>();
        List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantsByRating();

        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            RestaurantList restaurant = new RestaurantList();
            restaurant.setId(UUID.fromString(restaurantEntity.getUuid()));
            restaurant.setRestaurantName(restaurantEntity.getRestaurantName());
            restaurant.setPhotoURL(restaurantEntity.getPhotoUrl());
            restaurant.setCustomerRating(new BigDecimal(Double.toString(restaurantEntity.getCustomerRating())));
            restaurant.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());

            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
            address.setId(UUID.fromString((restaurantEntity.getAddress().getUuid())));
            address.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuilNo());
            address.setLocality(restaurantEntity.getAddress().getLocality());
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setPincode(restaurantEntity.getAddress().getPincode());
            RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
            state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
            state.setStateName(restaurantEntity.getAddress().getState().getStateName());
            address.setState(state);
            restaurant.setAddress(address);

            List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
            List<String> categoryNames = new ArrayList<>();
            for (CategoryEntity category : categoryEntityList) {
                categoryNames.add(category.getCategoryName());
            }
            Collections.sort(categoryNames);
            String categoryString = String.join(", ", categoryNames);
            restaurant.setCategories(categoryString);

            restaurantList.add(restaurant);

        }
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        restaurantListResponse.setRestaurants(restaurantList);
        return new ResponseEntity<>(restaurantListResponse, HttpStatus.OK);
    }

    @RequestMapping(path = "/name/{reastaurant_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurantDetails(@PathVariable("reastaurant_name") String name) throws RestaurantNotFoundException {
        List<RestaurantList> restaurantList = new ArrayList<RestaurantList>();
        List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantsByName(name);
        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            RestaurantList restaurant = new RestaurantList();
            restaurant.setId(UUID.fromString(restaurantEntity.getUuid()));
            restaurant.setRestaurantName(restaurantEntity.getRestaurantName());
            restaurant.setPhotoURL(restaurantEntity.getPhotoUrl());
            restaurant.setCustomerRating(new BigDecimal(Double.toString(restaurantEntity.getCustomerRating())));
            restaurant.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());

            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
            address.setId(UUID.fromString((restaurantEntity.getAddress().getUuid())));
            address.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuilNo());
            address.setLocality(restaurantEntity.getAddress().getLocality());
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setPincode(restaurantEntity.getAddress().getPincode());
            RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
            state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
            state.setStateName(restaurantEntity.getAddress().getState().getStateName());
            address.setState(state);
            restaurant.setAddress(address);

            List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
            List<String> categoryNames = new ArrayList<>();
            for (CategoryEntity category : categoryEntityList) {
                categoryNames.add(category.getCategoryName());
            }
            Collections.sort(categoryNames);
            String categoryString = String.join(", ", categoryNames);
            restaurant.setCategories(categoryString);

            restaurantList.add(restaurant);
        }

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        restaurantListResponse.setRestaurants(restaurantList);
        return new ResponseEntity<>(restaurantListResponse, HttpStatus.OK);
    }

    @RequestMapping(path="/category/{category_id}", method = RequestMethod.GET)
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategory(@PathVariable("category_id") String categoryId) throws CategoryNotFoundException {
        List<RestaurantList> restaurantList = new ArrayList<RestaurantList>();
        List<RestaurantEntity> restaurantEntityList = restaurantService.restaurantByCategory(categoryId);
        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            RestaurantList restaurant = new RestaurantList();
            restaurant.setId(UUID.fromString(restaurantEntity.getUuid()));
            restaurant.setRestaurantName(restaurantEntity.getRestaurantName());
            restaurant.setPhotoURL(restaurantEntity.getPhotoUrl());
            restaurant.setCustomerRating(new BigDecimal(Double.toString(restaurantEntity.getCustomerRating())));
            restaurant.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());

            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
            address.setId(UUID.fromString((restaurantEntity.getAddress().getUuid())));
            address.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuilNo());
            address.setLocality(restaurantEntity.getAddress().getLocality());
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setPincode(restaurantEntity.getAddress().getPincode());
            RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
            state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
            state.setStateName(restaurantEntity.getAddress().getState().getStateName());
            address.setState(state);
            restaurant.setAddress(address);

            List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(restaurantEntity.getUuid());
            List<String> categoryNames = new ArrayList<>();
            for (CategoryEntity category : categoryEntityList) {
                categoryNames.add(category.getCategoryName());
            }
            Collections.sort(categoryNames);
            String categoryString = String.join(", ", categoryNames);
            restaurant.setCategories(categoryString);

            restaurantList.add(restaurant);
        }

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        restaurantListResponse.setRestaurants(restaurantList);
        return new ResponseEntity<>(restaurantListResponse, HttpStatus.OK);
    }



    @RequestMapping(path="/{restaurant_id}",method = RequestMethod.GET)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantById(@PathVariable("restaurant_id") String uuid) throws RestaurantNotFoundException, CategoryNotFoundException {
        RestaurantDetailsResponse restaurant = new RestaurantDetailsResponse();

        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(uuid);

        restaurant.setId(UUID.fromString(restaurantEntity.getUuid()));
        restaurant.setRestaurantName(restaurantEntity.getRestaurantName());
        restaurant.setPhotoURL(restaurantEntity.getPhotoUrl());
        restaurant.setCustomerRating(new BigDecimal(Double.toString(restaurantEntity.getCustomerRating())));
        restaurant.setAveragePrice(restaurantEntity.getAveragePriceForTwo());
        restaurant.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());
        RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
        address.setId(UUID.fromString((restaurantEntity.getAddress().getUuid())));
        address.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuilNo());
        address.setLocality(restaurantEntity.getAddress().getLocality());
        address.setCity(restaurantEntity.getAddress().getCity());
        address.setPincode(restaurantEntity.getAddress().getPincode());
        RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
        state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
        state.setStateName(restaurantEntity.getAddress().getState().getStateName());
        address.setState(state);
        restaurant.setAddress(address);

        List<CategoryEntity> categoryEntityList = categoryService.getCategoriesByRestaurant(uuid);
        List<CategoryList> categoryList = new ArrayList<>();
        for (CategoryEntity ce: categoryEntityList) {
            CategoryList category = new CategoryList();
            category.setId(UUID.fromString(ce.getUuid()));
            category.setCategoryName(ce.getCategoryName());
            List<ItemEntity> itemEntityList = itemService.getItemsByCategoryAndRestaurant(uuid,ce.getUuid());
            List<ItemList> itemListList = new ArrayList<>();
            for (ItemEntity itemEntity: itemEntityList) {
                ItemList item = new ItemList();
                item.setId(UUID.fromString(itemEntity.getUuid()));
                item.setItemName(itemEntity.getItemName());
                item.setPrice(itemEntity.getPrice());
                item.setItemType(ItemList.ItemTypeEnum.fromValue(itemEntity.getType().getValue()));
                itemListList.add(item);
            }
            category.setItemList(itemListList);
            categoryList.add(category);
        }
        restaurant.categories(categoryList);

        return new ResponseEntity<RestaurantDetailsResponse>(restaurant, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(
        @RequestParam(name = "customer_rating") final Double customerRating,
        @PathVariable("restaurant_id") final String restaurantId,
        @RequestHeader("authorization") final String authorization)
        throws RestaurantNotFoundException, AuthorizationFailedException, InvalidRatingException {
        final String accessToken = AppUtils.getBearerAuthToken(authorization);
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        RestaurantEntity restaurant = restaurantService.restaurantByUUID(restaurantId);
        RestaurantEntity updatedRestaurant = restaurantService.updateRestaurantRating(restaurant,customerRating);
        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse().id(UUID.fromString(restaurantId)).status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<>(restaurantUpdatedResponse, HttpStatus.OK);
    }
}


