package com.example.inventoryapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.inventoryapp.R;
import com.example.inventoryapp.adapter.HistroyAdapter;
import com.example.inventoryapp.databinding.ActivityHistoryBinding;
import com.example.inventoryapp.model.AssetDetails;
import com.example.inventoryapp.retrofit.APIClient;
import com.example.inventoryapp.retrofit.APIInterface;
import com.example.inventoryapp.retrofit.response.RestResponseAssetDetialsList;
import com.example.inventoryapp.utils.SessionManager;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BaseActivity {

    ActivityHistoryBinding binding;
    ArrayList<AssetDetails> assetDetails = new ArrayList<>();
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        setData();
    }

    public void setData() {
        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        sessionManager = new SessionManager(HistoryActivity.this);
        qtyHistroyAPI();
    }

    public void setAdapter(ArrayList<AssetDetails> assetDetails) {
        if (assetDetails.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryActivity.this);
            binding.rv.setLayoutManager(linearLayoutManager);
            HistroyAdapter adapter = new HistroyAdapter(assetDetails, HistoryActivity.this);
            binding.rv.setAdapter(adapter);
        } else {
            binding.rv.setVisibility(View.GONE);
            binding.tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void qtyHistroyAPI() {
        if (!isInternetOn(HistoryActivity.this)) {
            showToast(getString(R.string.check_internet));
            return;
        }
        showCustomLoader(HistoryActivity.this);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RestResponseAssetDetialsList> callApi = apiInterface.updateQuantityHistory(String.valueOf(sessionManager.getLogin().getUser_id()));
        callApi.enqueue(new Callback<RestResponseAssetDetialsList>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NotNull Call<RestResponseAssetDetialsList> call, @NotNull Response<RestResponseAssetDetialsList> response) {

                Log.e("qtyHistroy", "------" + response.message());
                Log.e("qtyHistroy", "------" + response.code());
                if (response.body() != null) {
                    dismissCustomLoader();
                    Log.e("qtyHistroy Response ::", new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        assetDetails.clear();
                        assetDetails = new ArrayList<>();
                        assetDetails = response.body().getResult();
                        setAdapter(assetDetails);
                        //Toast.makeText(HistoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus() == 0) {
                        binding.rv.setVisibility(View.GONE);
                        binding.tvNoData.setVisibility(View.VISIBLE);
                        //Toast.makeText(HistoryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(HistoryActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    dismissCustomLoader();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestResponseAssetDetialsList> call, @NotNull Throwable t) {
                dismissCustomLoader();
                Log.e("Error >> ", t.getMessage());
                Toast.makeText(HistoryActivity.this, "qtyHistroy In Failed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}