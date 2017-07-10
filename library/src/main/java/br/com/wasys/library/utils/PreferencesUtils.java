package br.com.wasys.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

import br.com.wasys.library.Application;

/**
 * Created by pascke on 11/08/16.
 */
public class PreferencesUtils {

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        return get(String.class, key, defaultValue);
    }

    public static <T> T get(Class<T> clazz, String key) {
        return get(clazz, key, null);
    }

    public static <T> T get(Class<T> clazz, String key, T defaultValue) {
        T value = defaultValue;
        if (clazz != null && StringUtils.isNotBlank(key)) {
            SharedPreferences preferences = getPreferences();
            String text = preferences.getString(key, null);
            if (StringUtils.isNotBlank(text)) {
                value = TypeUtils.parse(clazz, text);
            }
        }
        return value;
    }

    public static void put(String key, Object value) {
        if (StringUtils.isNotBlank(key)) {
            if (value == null) {
                remove(key);
            }
            else {
                String text;
                if (value instanceof Date) {
                    text = DateUtils.format((Date) value);
                }
                else {
                    text = String.valueOf(value);
                }
                SharedPreferences.Editor editor = getEditor();
                editor.putString(key, text);
                editor.commit();
            }
        }
    }

    public static <T> T getJSON(Class<T> clazz,String key) {
        if (StringUtils.isNotBlank(key)) {
            SharedPreferences preferences = getPreferences();
            String text = preferences.getString(key, null);
            if (StringUtils.isNotBlank(text)) {
                try {
                    return JacksonUtils.getObjectMapper().readValue(text, clazz);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void putJSON(String key, Object value) {
        if (StringUtils.isNotBlank(key)) {
            if (value == null) {
                remove(key);
            }
            else {
                try {
                    String text = JacksonUtils.getObjectMapper().writeValueAsString(value);
                    SharedPreferences.Editor editor = getEditor();
                    editor.putString(key, text);
                    editor.commit();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.commit();
    }

    public static String getKeyFileName() {
        Context context = Application.getContext();
        String packageName = context.getPackageName();
        String keyFileName = packageName + ".PREFERENCE_FILE_KEY";
        return keyFileName;
    }

    public static SharedPreferences getPreferences() {
        Context context = Application.getContext();
        String keyFileName = getKeyFileName();
        SharedPreferences preferences = context.getSharedPreferences(keyFileName, Context.MODE_PRIVATE);
        return preferences;
    }

    private static SharedPreferences.Editor getEditor() {
        SharedPreferences preferences = getPreferences();
        return preferences.edit();
    }
}
