package com.nova.simple.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;

public class Pref {

    public static void save(Context context, String generalKey, Map<String, Object> map) {
        SharedPreferences preferences =
                context.getSharedPreferences(generalKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        if (!map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                edit.putString(key, value != null ? value.toString() : null).apply();
            }
        }
    }

    public static String load(
            Context context, String generalKey, String keyValue, String defaultValue) {
        return context.getSharedPreferences(generalKey, Context.MODE_PRIVATE)
                .getString(keyValue, defaultValue);
    }

    public static void remove(Context context, String generalKey) {
        context.getSharedPreferences(generalKey, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
