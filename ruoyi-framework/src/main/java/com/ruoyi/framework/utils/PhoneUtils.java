package com.ruoyi.framework.utils;

public class PhoneUtils {
    public static boolean isMobile(String str) {
        return str != null && str.matches("^1[3-9]\\d{9}$");
    }
}