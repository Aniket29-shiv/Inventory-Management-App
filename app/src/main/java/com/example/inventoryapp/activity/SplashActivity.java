package com.example.inventoryapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.inventoryapp.R;
import com.example.inventoryapp.databinding.ActivitySplashBinding;
import com.example.inventoryapp.retrofit.APIClient;
import com.example.inventoryapp.retrofit.APIInterface;
import com.example.inventoryapp.retrofit.response.RestResponse;
import com.example.inventoryapp.utils.Constant;
import com.example.inventoryapp.utils.SessionManager;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    ActivitySplashBinding binding;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*
                Intent mainIntent = new Intent(SplashActivity.this, SplashActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/
        setData();
    }

    public void setData() {
        sessionManager = new SessionManager(SplashActivity.this);
        if (sessionManager.getBooleanValue(Constant.IS_ALREADY_LOGIN)) {
            sendSignInData(sessionManager.getPref(Constant.EMAIL), sessionManager.getPref(Constant.PASSWORD));
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }

    }

    private void sendSignInData(String email, String password) {
        if (!isInternetOn(SplashActivity.this)) {
            showToast(getString(R.string.check_internet));
            return;
        }
        showCustomLoader(SplashActivity.this);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Log.e("SignIn Data ::", "Email:" + email + " password:" + password);

        Call<RestResponse> callApi = apiInterface.login(email, password);
        callApi.enqueue(new Callback<RestResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NotNull Call<RestResponse> call, @NotNull Response<RestResponse> response) {

                Log.e("LOGIN", "------" + response.message());
                Log.e("LOGIN", "------" + response.code());
                if (response.body() != null) {
                    dismissCustomLoader();
                    Log.e("Sign In Data Response ::", new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        sessionManager.setData(Constant.IS_ALREADY_LOGIN, true);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(SplashActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {


                        Toast.makeText(SplashActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SplashActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    dismissCustomLoader();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestResponse> call, @NotNull Throwable t) {
                dismissCustomLoader();
                Log.e("Error >> ", t.getMessage());
                Toast.makeText(SplashActivity.this, "Sign In Failed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}