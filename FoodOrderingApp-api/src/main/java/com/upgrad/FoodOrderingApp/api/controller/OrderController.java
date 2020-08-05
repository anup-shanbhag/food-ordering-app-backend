package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.business.OrderService;
import com.upgrad.FoodOrderingApp.service.common.AppUtils;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.CPF_002;

@RestController
@RequestMapping("/order")
public class OrderController {
    // TODO :
    //  - Get Past Orders of User - “/order”
    //  - Save Order - “/order”
    //  - Get Payment Methods - “/payment”

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(path = "/coupon/{coupon_name}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(@RequestHeader("authorization") final String headerParam,@PathVariable("coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {
        final String accessToken = AppUtils.getBearerAuthToken(headerParam);
        final CustomerAuthEntity customerAuthEntity = customerService.getCustomerAuthenticationByAccessToken(accessToken);
        final CouponEntity coupon = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse response = new CouponDetailsResponse();
        response.id(UUID.fromString(coupon.getUuid())).couponName(coupon.getCouponName()).percent(coupon.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(response,HttpStatus.OK);
    }

}
