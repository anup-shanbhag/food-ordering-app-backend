package com.upgrad.FoodOrderingApp.service.common;

import org.apache.commons.lang3.StringUtils;

public class AppUtils {

    /**
     * Takes a basic authorization token removes prefix
     * @param headerParam Basic Authorization Token
     * @return Basic Authorization Token with Prefix Removed
     */
    public static String getBearerAuthToken(String headerParam){
        return (headerParam.contains("Bearer ")) ? StringUtils.substringAfter(headerParam,"Bearer ") : headerParam;
    }

    /**
     * Takes a bearer authorization token removes prefix
     * @param headerParam Bearer Authorization Token
     * @return Bearer Authorization Token with Prefix Removed
     */
    public static String getBasicAuthToken(String headerParam){
        return (headerParam.contains("Basic ")) ? StringUtils.substringAfter(headerParam,"Basic ") : headerParam;
    }

}
