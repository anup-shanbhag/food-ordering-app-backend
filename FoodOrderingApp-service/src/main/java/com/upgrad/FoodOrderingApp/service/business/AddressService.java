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
            throw new SaveAddressException("SAR-001", "No field can be empty");
        if (!validPincode(address.getPincode())) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }
        address.setState(state);

        return addressDao.saveAddress(address);
    }

    public StateEntity getStateByUUID(final String stateUUID) throws AddressNotFoundException {
        StateEntity state = stateDao.findStateByUUID(stateUUID);
        if (state == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }
        return state;
    }

    private boolean addressFieldsEmpty(AddressEntity address) {
        if (address.getFlatBuilNo().isEmpty() ||
                address.getLocality().isEmpty() ||
                address.getCity().isEmpty() ||
                address.getPincode().isEmpty())
            return true;
        return false;
    }

    private boolean validPincode(String pincode) throws SaveAddressException {
        Pattern p = Pattern.compile("\\d{6}\\b");
        Matcher m = p.matcher(pincode);
        return (m.find() && m.group().equals(pincode));

    }
}
