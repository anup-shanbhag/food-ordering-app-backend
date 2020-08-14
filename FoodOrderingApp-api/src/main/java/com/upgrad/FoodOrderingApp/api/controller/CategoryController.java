package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.common.UnexpectedException;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Method takes input from customer, returns all categories
     * @return ResponseEntity with list of Categories
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategoriesOrderedByName() {

        // Retrieve categories list from database
        List<CategoryEntity> categoryEntityList = categoryService.getAllCategoriesOrderedByName();

        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();

        // Map retrieved Category Entity to Response Object List
        categoryEntityList.forEach(category ->
                categoriesListResponse.addCategoriesItem(
                        new CategoryListResponse()
                                .id(UUID.fromString(category.getUuid()))
                                .categoryName(category.getCategoryName())
                ));

        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    /**
     * Method takes categoryId from customer, returns category from database
     * @param categoryId category id as request path var
     * @return ResponseEntity with Category details
     * @throws CategoryNotFoundException on invalid category id
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(
            @PathVariable("category_id") final String categoryId) throws CategoryNotFoundException {

        // Retrieve category from database
        CategoryEntity categoryEntity = categoryService.getCategoryById(categoryId);

        ArrayList<ItemList> itemList = new ArrayList<>();

        // Map retrieved item Entity to Item Object List
        categoryEntity.getItems().forEach(items ->
                itemList.add(
                        new ItemList()
                                .id(UUID.fromString(items.getUuid()))
                                .itemName(items.getItemName())
                                .itemType(ItemList.ItemTypeEnum.fromValue(items.getType().getValue()))
                                .price(items.getPrice())
                ));

        // Map retrieved Category Entity to Response Object List
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse()
                .categoryName(categoryEntity.getCategoryName())
                .id(UUID.fromString(categoryEntity.getUuid()))
                .itemList(itemList);

        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }
}
