package com.nova.simple;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class ThemeManager {
    private ThemeManager() {}

    public enum Mode {
        DEFAULT,
        DARK,
        LIGHT
    }

    public static void applyTheme(Mode mode) {
        switch (mode) {
            case DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                } else {
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
        }
    }

    public static void applyTheme(Context context) {
        SharedPreferences defaultSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String value = defaultSharedPreferences.getString("tema_preference", Mode.DEFAULT.name());
        applyTheme(Mode.valueOf(value));
    }
}
