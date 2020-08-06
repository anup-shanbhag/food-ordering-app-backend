package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.business.OrderService;
import com.upgrad.FoodOrderingApp.service.common.AppUtils;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.CPF_002;

@RestController
@RequestMapping("/order")
public class OrderController {
    // TODO :
    //  - Save Order - “/order”

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(path = "/coupon/{coupon_name}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(@RequestHeader("authorization") final String headerParam, @PathVariable("coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {
        final String accessToken = AppUtils.getBearerAuthToken(headerParam);
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final CouponEntity coupon = orderService.getCouponByCouponName(couponName);
        CouponDetailsResponse response = new CouponDetailsResponse();
        response.id(UUID.fromString(coupon.getUuid())).couponName(coupon.getCouponName()).percent(coupon.getPercent());
        return new ResponseEntity<CouponDetailsResponse>(response,HttpStatus.OK);
    }

    @RequestMapping(path = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerOrderResponse> getOrders(@RequestHeader("authorization") final String headerParam) throws AuthorizationFailedException {
        final String accessToken = AppUtils.getBearerAuthToken(headerParam);
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        CustomerOrderResponse response = new CustomerOrderResponse();
        List<OrderEntity> orderEntities = orderService.getOrdersByCustomers(customerEntity.getUuid());
        orderEntities.forEach( orderEntity -> {
            OrderList orderList = new OrderList()
                    .id(UUID.fromString(orderEntity.getUuid()))
                    .customer(new OrderListCustomer()
                            .id(UUID.fromString(orderEntity.getCustomer().getUuid()))
                            .firstName(orderEntity.getCustomer().getFirstName())
                            .lastName(orderEntity.getCustomer().getLastName())
                            .emailAddress(orderEntity.getCustomer().getEmail())
                            .contactNumber(orderEntity.getCustomer().getContactNumber()))
                    .bill(BigDecimal.valueOf(orderEntity.getBill()))
                    .discount(BigDecimal.valueOf(orderEntity.getDiscount()))
                    .date(orderEntity.getDate().toString())
                    .address( new OrderListAddress()
                            .id(UUID.fromString(orderEntity.getAddress().getUuid()))
                            .flatBuildingName(orderEntity.getAddress().getUuid())
                            .locality(orderEntity.getAddress().getLocality())
                            .city(orderEntity.getAddress().getCity())
                            .pincode(orderEntity.getAddress().getPincode()));
                    if(orderEntity.getAddress().getState()!=null){
                        orderList.getAddress().state( new OrderListAddressState()
                                .id(UUID.fromString(orderEntity.getAddress().getState().getUuid()))
                                .stateName(orderEntity.getAddress().getState().getStateName()));
                    }
            if(orderEntity.getCoupon() != null){
                orderList.coupon(new OrderListCoupon()
                        .id(UUID.fromString(orderEntity.getCoupon().getUuid()))
                        .couponName(orderEntity.getCoupon().getCouponName())
                        .percent(orderEntity.getCoupon().getPercent()));
            }
            if(orderEntity.getPayment() != null){
                orderList.payment( new OrderListPayment()
                        .id(UUID.fromString(orderEntity.getPayment().getUuid()))
                        .paymentName(orderEntity.getPayment().getPaymentName()));
            }
            orderEntity.getItems().forEach( orderItemEntity -> {
                orderList.addItemQuantitiesItem( new ItemQuantityResponse()
                        .item( new ItemQuantityResponseItem()
                                .id(UUID.fromString(orderItemEntity.getItem().getUuid()))
                                .itemName(orderItemEntity.getItem().getItemName())
                                .itemPrice(orderItemEntity.getItem().getPrice())
                                .type(ItemQuantityResponseItem.TypeEnum.fromValue(orderItemEntity.getItem().getType().toString())))
                        .price(orderItemEntity.getPrice())
                        .quantity(orderItemEntity.getQuantity()));
            });
            response.addOrdersItem(orderList);
        });

        if(response.getOrders()==null || response.getOrders().isEmpty()){
            return new ResponseEntity<CustomerOrderResponse>(response, HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<CustomerOrderResponse>(response, HttpStatus.OK);
        }
    }

}
