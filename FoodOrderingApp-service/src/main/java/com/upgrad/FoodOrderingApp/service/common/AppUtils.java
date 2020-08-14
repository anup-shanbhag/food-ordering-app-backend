package com.upgrad.FoodOrderingApp.service.common;

import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

import static com.upgrad.FoodOrderingApp.service.common.GenericErrorCode.GEN_001;

public class AppUtils {

    /**
     * Takes a basic authorization token removes prefix
     * @param headerParam Basic Authorization Token
     * @return Basic Authorization Token with Prefix Removed
     */
    public static String getBearerAuthToken(String headerParam){
        String bearerToken = (headerParam.contains("Bearer ")) ? StringUtils.substringAfter(headerParam,"Bearer ") : headerParam;
        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new UnexpectedException(GEN_001);
        }
        else {
            return bearerToken;
        }
    }

    /**
     * Takes a bearer authorization token removes prefix
     * @param headerParam Bearer Authorization Token
     * @return Bearer Authorization Token with Prefix Removed
     */
    public static String getBasicAuthToken(String headerParam){
        String basicToken = (headerParam.contains("Basic ")) ? StringUtils.substringAfter(headerParam,"Basic ") : headerParam;
        if (basicToken == null || basicToken.isEmpty()) {
            throw new UnexpectedException(GEN_001);
        }
        else {
            return new String(Base64.getDecoder().decode(basicToken));
        }
    }

}
