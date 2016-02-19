package com.sixlabs.atsys.domain.utils;

public class NumberUtils {

    public static Long toLong(String text) {
        try {
            return text != null ? Long.decode(text) : null;

        } catch (Exception e) {
            return null;
        }
    }

}
