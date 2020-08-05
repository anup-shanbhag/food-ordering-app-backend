package com.upgrad.FoodOrderingApp.service.business;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.*;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private StateDao stateDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity address, StateEntity state) throws AddressNotFoundException, SaveAddressException {
        if (addressFieldsEmpty(address))
            throw new SaveAddressException(SAR_001.getCode(), SAR_001.getDefaultMessage());
        if (!validPincode(address.getPincode())) {
            throw new SaveAddressException(SAR_002.getCode(), SAR_002.getDefaultMessage());
        }
        address.setState(state);

        return addressDao.saveAddress(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) {

        List<AddressEntity> addresses = customerEntity.getAddresses();
        Collections.sort(addresses);

        return addresses;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getAllStates() {

        return stateDao.getAllStates();
    }

    public AddressEntity getAddressByUUID(String addressId, CustomerEntity customerEntity)
            throws AddressNotFoundException, AuthorizationFailedException {
        if (addressId == null) {
            throw new AddressNotFoundException(ANF_005.getCode(), ANF_005.getDefaultMessage());
        }
        AddressEntity address = addressDao.getAddressByAddressId(addressId);
        if (address == null) {
            throw new AddressNotFoundException(ANF_003.getCode(), ANF_003.getDefaultMessage());
        }

        if (!address.getCustomers().getUuid().equals(customerEntity.getUuid())) {
            throw new AuthorizationFailedException(ATHR_004.getCode(),ATHR_004.getDefaultMessage());
        }
        return address;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(final AddressEntity address) {
        return addressDao.deleteAddress(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deactivateAddress(final AddressEntity address) {
        return addressDao.deactivateAddress(address);
    }

    public StateEntity getStateByUUID(final String stateUUID) throws AddressNotFoundException {
        StateEntity state = stateDao.findStateByUUID(stateUUID);
        if (state == null) {
            throw new AddressNotFoundException(ANF_002.getCode(), ANF_002.getDefaultMessage());
        }
        return state;
    }

    private boolean addressFieldsEmpty(AddressEntity address) {
        return address.getFlatBuilNo().isEmpty() ||
                address.getLocality().isEmpty() ||
                address.getCity().isEmpty() ||
                address.getPincode().isEmpty();
    }

    private boolean validPincode(String pincode) throws SaveAddressException {
        Pattern p = Pattern.compile("\\d{6}\\b");
        Matcher m = p.matcher(pincode);
        return (m.find() && m.group().equals(pincode));
    }
}
