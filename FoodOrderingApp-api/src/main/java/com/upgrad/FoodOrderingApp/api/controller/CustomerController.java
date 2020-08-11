package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.business.CustomerService;
import com.upgrad.FoodOrderingApp.service.common.AppConstants;
import com.upgrad.FoodOrderingApp.service.common.AppUtils;
import com.upgrad.FoodOrderingApp.service.common.UnexpectedException;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.*;

@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Method takes Customer's Signup request, stores customer information in the system
     * @param request Customer's signup request having  details like name, email, contact etc.
     * @return ResponseEntity with Customer Id
     * @throws SignUpRestrictedException on invalid signup request or customer already registered
     * @throws UnexpectedException on any other errors
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/signup",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SignupCustomerResponse> registerCustomer(@RequestBody(required = false) final SignupCustomerRequest request) throws SignUpRestrictedException, UnexpectedException {
        validateSignupRequest(request);
        final CustomerEntity newCustomerEntity = new CustomerEntity();
        newCustomerEntity.setUuid(UUID.randomUUID().toString());
        newCustomerEntity.setFirstName(request.getFirstName());
        newCustomerEntity.setLastName(request.getLastName());
        newCustomerEntity.setEmail(request.getEmailAddress());
        newCustomerEntity.setPassword(request.getPassword());
        newCustomerEntity.setContactNumber(request.getContactNumber());
        newCustomerEntity.setSalt(UUID.randomUUID().toString());
        final CustomerEntity customerEntity = customerService.saveCustomer(newCustomerEntity);
        final SignupCustomerResponse response = new SignupCustomerResponse();
        response.id(customerEntity.getUuid()).status("CUSTOMER CREATED SUCCESSFULLY");
        return new ResponseEntity<SignupCustomerResponse>(response, HttpStatus.CREATED);
    }

    /**
     * Method takes customers username (contact number) and logs the user into the system
     * @param headerParam Basic authorization token with username & password as a request header param
     * @return ResponseEntity with Customer Id, Name, Contact, Email & Access Token
     * @throws AuthenticationFailedException on incorrect/invalid credentials
     * @throws UnexpectedException on any other errors
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> loginCustomer(@RequestHeader("authorization") final String headerParam) throws AuthenticationFailedException, UnexpectedException {
        final String authToken = new String(Base64.getDecoder().decode(AppUtils.getBasicAuthToken(headerParam)));
        validateLoginRequest(authToken);
        StringTokenizer tokens =  new StringTokenizer(authToken, AppConstants.COLON);
        final CustomerAuthEntity customerAuthEntity = customerService.authenticate(tokens.nextToken(),tokens.nextToken());
        final LoginResponse response = new LoginResponse();
        response.id(customerAuthEntity.getCustomer().getUuid()).firstName(customerAuthEntity.getCustomer().getFirstName()).lastName(customerAuthEntity.getCustomer().getLastName()).contactNumber(customerAuthEntity.getCustomer().getContactNumber()).emailAddress(customerAuthEntity.getCustomer().getEmail()).message("LOGGED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add(AppConstants.HTTP_ACCESS_TOKEN_HEADER,customerAuthEntity.getAccessToken());
        headers.setAccessControlExposeHeaders(Collections.singletonList(AppConstants.HTTP_ACCESS_TOKEN_HEADER));
        return new ResponseEntity<LoginResponse>(response, headers, HttpStatus.OK);
    }

    /**
     * Methods takes a customer's access token and logs the user out of the application
     * @param headerParam Customer's access token as a request header parameter
     * @return Customer's Id
     * @throws AuthorizationFailedException on invalid/incorrect access token
     * @throws UnexpectedException on any other errors
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LogoutResponse> logoutCustomer(@RequestHeader("authorization") final String headerParam) throws AuthorizationFailedException, UnexpectedException {
        final String accessToken = AppUtils.getBearerAuthToken(headerParam);
        final CustomerAuthEntity customerAuthEntity = customerService.logout(accessToken);
        final LogoutResponse response = new LogoutResponse();
        response.id(customerAuthEntity.getCustomer().getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(response, HttpStatus.OK);
    }

    /**
     * Method takes updated customer information and updates it in the system
     * @param headerParam Customer's access token as a request header parameter
     * @param request Updated Customer Information like Name
     * @return ResponseEntity with updated customer name
     * @throws AuthorizationFailedException on invalid/incorrect access token
     * @throws UpdateCustomerException on invalid customer information
     * @throws UnexpectedException on any other errors
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization") final String headerParam, @RequestBody(required = false) final UpdateCustomerRequest request) throws UnexpectedException, AuthorizationFailedException, UpdateCustomerException {
        validateUpdateCustomerRequest(request);
        final String accessToken = AppUtils.getBearerAuthToken(headerParam);
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        customerEntity.setFirstName(request.getFirstName());
        customerEntity.setLastName(request.getLastName());
        final CustomerEntity updatedCustomerEntity = customerService.updateCustomer(customerEntity);
        final UpdateCustomerResponse response = new UpdateCustomerResponse();
        response.id(updatedCustomerEntity.getUuid()).firstName(updatedCustomerEntity.getFirstName()).lastName(updatedCustomerEntity.getLastName()).status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdateCustomerResponse>(response, HttpStatus.OK);
    }

    /**
     * Methods takes updated password information from the customer and updates it in the system
     * @param headerParam Customer's access token as request header param
     * @param request Customer's Current & New Passwords
     * @return Customer id
     * @throws AuthorizationFailedException on incorrect/invalid access token
     * @throws UpdateCustomerException on incorrect/invalid old/new password
     * @throws UnexpectedException on any other errors
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(@RequestHeader("authorization") final String headerParam, @RequestBody(required = false) final UpdatePasswordRequest request) throws UnexpectedException, AuthorizationFailedException, UpdateCustomerException {
        validatePasswordChangeRequest(request);
        final String accessToken = AppUtils.getBearerAuthToken(headerParam);
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final CustomerEntity updatedCustomerEntity = customerService.updateCustomerPassword(request.getOldPassword(),request.getNewPassword(),customerEntity);
        final UpdatePasswordResponse response = new UpdatePasswordResponse();
        response.id(updatedCustomerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(response, HttpStatus.OK);
    }

    /**
     * Method validates if customer's sign up request has all necessary information
     * @param request Customer's Signup Request
     * @throws SignUpRestrictedException when one ore more of first name, password, email & contact number are missing on the request
     */
    private void validateSignupRequest(SignupCustomerRequest request) throws SignUpRestrictedException {
        if((request.getContactNumber() == null) || (request.getFirstName() == null) ||
                (request.getPassword() == null) || (request.getEmailAddress() == null) ||
                (request.getContactNumber().isEmpty()) || (request.getFirstName().isEmpty()) ||
                (request.getEmailAddress().isEmpty()) || (request.getPassword().isEmpty())){
            throw new SignUpRestrictedException(SGR_005.getCode(), SGR_005.getDefaultMessage());
        }
    }

    /**
     * Method validates if customer's sign up request has all necessary information
     * @param authorizationToken Customer's Signin Request
     * @throws AuthenticationFailedException on incorrect/invalid basic authentication token
     */
    private void validateLoginRequest(String authorizationToken) throws AuthenticationFailedException {
        if(!authorizationToken.matches(AppConstants.REG_EXP_BASIC_AUTH)){
            throw new AuthenticationFailedException(ATH_003.getCode(),ATH_003.getDefaultMessage());
        }
    }

    /**
     * Method validates if customer's update request has all necessary information
     * @param request Customer's Update Information request
     * @throws UpdateCustomerException when first name is missing on the request
     */
    private void validateUpdateCustomerRequest(UpdateCustomerRequest request) throws UpdateCustomerException {
        if(request.getFirstName()==null || request.getFirstName().isEmpty()){
            throw new UpdateCustomerException(UCR_002.getCode(),UCR_002.getDefaultMessage());
        }
    }

    /**
     * Method validates if customer's password change request has all necessary information
     * @param request Customers Password Change request
     * @throws UpdateCustomerException when old password or new password or both are missing on the input request
     */
    private void validatePasswordChangeRequest(UpdatePasswordRequest request) throws UpdateCustomerException {
        if((request.getOldPassword() == null) || (request.getNewPassword() == null) || (request.getOldPassword().isEmpty()) || (request.getNewPassword().isEmpty())){
            throw new UpdateCustomerException(UCR_003.getCode(), UCR_003.getDefaultMessage());
        }
    }
}
