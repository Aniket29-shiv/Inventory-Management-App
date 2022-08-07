package com.example.inventoryapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inventoryapp.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.List;

public class SessionManager {
    private final SharedPreferences sharedPreferences;
    private final Context context;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setStringValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }


    public void saveLoginData(User user) {
        Gson gson = new Gson();
        String personString = gson.toJson(user);
        editor = sharedPreferences.edit();
        editor.putString(Constant.LOGIN_USER_DATA, personString);
        editor.apply();
    }


    public User getLogin() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constant.LOGIN_USER_DATA, null);

        Type type = new TypeToken<User>() {
        }.getType();
        return gson.fromJson(json, type);
    }




    public void setBooleanValue(String key, Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBooleanValue(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPref(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public void setData(String key, Object value) {

        editor = sharedPreferences.edit();
        delete(key);
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }
        editor.commit();
    }

    public void delete(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit();
        }
    }
    public void logoutUser() {

        sharedPreferences.edit().clear().commit();


    }


}

