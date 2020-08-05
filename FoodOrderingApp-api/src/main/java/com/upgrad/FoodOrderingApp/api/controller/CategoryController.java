package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.business.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategoriesOrderedByName() {

        List<CategoryEntity> categoryEntityList = categoryService.getAllCategoriesOrderedByName();

        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();

        categoryEntityList.forEach(category ->
                categoriesListResponse.addCategoriesItem(
                        new CategoryListResponse()
                                .id(UUID.fromString(category.getUuid()))
                                .categoryName(category.getCategoryName())
                ));

        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }
}
