package com.example.demo.utils;

public class UserContext {
    private static final ThreadLocal<String> userType = new ThreadLocal<>();

    public static void setUserType(String type) {
        userType.set(type);
    }

    public static String getUserType() {
        return userType.get();
    }

    public static void clear() {
        userType.remove();
    }
}
