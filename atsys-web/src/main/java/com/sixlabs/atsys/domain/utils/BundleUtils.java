package com.sixlabs.atsys.domain.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Classe utilitária para obter mensagens da aplicação internacionalizadas.
 */
public class BundleUtils {

    public static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("Messages", locale);
    }

    public static ResourceBundle getBundle() {
        return getBundle(Locale.getDefault());
    }

    public static String getMsg(@NotNull String key, @Nullable Locale locale) {
        return locale != null ? getBundle(locale).getString(key) : getMsg(key) ;
    }

    public static String getMsg(@NotNull String key) {
        return getMsg(key, Locale.getDefault());
    }

}
