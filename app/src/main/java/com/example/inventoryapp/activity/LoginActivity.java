package com.example.inventoryapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import com.example.inventoryapp.R;
import com.example.inventoryapp.databinding.ActivityLoginBinding;
import com.example.inventoryapp.databinding.DialogAssestNumberBinding;
import com.example.inventoryapp.retrofit.APIClient;
import com.example.inventoryapp.retrofit.APIInterface;
import com.example.inventoryapp.retrofit.response.RestResponse;
import com.example.inventoryapp.utils.Constant;
import com.example.inventoryapp.utils.SessionManager;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    private boolean show_password = false;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        setData();
    }

    public void setData() {

        sessionManager = new SessionManager(LoginActivity.this);
        binding.ivEye.setOnClickListener(v -> {
            if (show_password == false) {
                binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.ivEye.setImageResource(R.drawable.enable_eyes);
                show_password = true;
            } else {
                binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.ivEye.setImageResource(R.drawable.disable_eye);
                show_password = false;
            }
            binding.edtPassword.setSelection(binding.edtPassword.length());
        });
        binding.btnLogin.setOnClickListener(v -> {
            checkValidation();

        });
    }

    public void checkValidation() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = Objects.requireNonNull(binding.edtEmail.getText()).toString();
        String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
        if (email.isEmpty()) {

            binding.edtEmail.setError("Enter username");
            binding.edtEmail.requestFocus();

        } else if (!email.matches(emailPattern)) {
            binding.edtEmail.setError("Enter valid email");
            binding.edtEmail.requestFocus();
        } else if (password.isEmpty()) {
            binding.edtPassword.setError("Enter password");
            binding.edtPassword.requestFocus();

        } else {
            sendSignInData(email, password);
        }
    }

    private void sendSignInData(String email, String password) {
        if (!isInternetOn(LoginActivity.this)) {
            showToast(getString(R.string.check_internet));
            return;
        }
        showCustomLoader(LoginActivity.this);
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
                        sessionManager.setData(Constant.EMAIL, response.body().getResult().getEmail());
                        sessionManager.setData(Constant.PASSWORD, password);
                        sessionManager.saveLoginData(response.body().getResult());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                       // Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {


                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    dismissCustomLoader();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestResponse> call, @NotNull Throwable t) {
                dismissCustomLoader();
                Log.e("Error >> ", t.getMessage());
                Toast.makeText(LoginActivity.this, "Sign In Failed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}