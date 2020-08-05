package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.CPF_001;
import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.CPF_002;

@Service
public class OrderService {

    @Autowired
    CouponDao couponDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponName(final String couponName) throws CouponNotFoundException {
        if((couponName == null) || (couponName.isEmpty())){
            throw new CouponNotFoundException(CPF_002.getCode(),CPF_002.getDefaultMessage());
        }
        final CouponEntity couponEntity = couponDao.getCouponByCouponName(couponName);
        if(couponEntity != null){
            return couponEntity;
        }
        else{
            throw new CouponNotFoundException(CPF_001.getCode(),CPF_001.getDefaultMessage());
        }
    }
}
