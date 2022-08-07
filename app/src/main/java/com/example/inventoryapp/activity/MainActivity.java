package com.example.inventoryapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.inventoryapp.R;
import com.example.inventoryapp.databinding.ActivityMainBinding;
import com.example.inventoryapp.databinding.DialogAssestNumberBinding;
import com.example.inventoryapp.databinding.DialogYesNoBinding;
import com.example.inventoryapp.retrofit.APIClient;
import com.example.inventoryapp.retrofit.APIInterface;
import com.example.inventoryapp.retrofit.response.RestResponseAssetDetials;
import com.example.inventoryapp.utils.Constant;
import com.example.inventoryapp.utils.SessionManager;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    Dialog dialog;
    Dialog alertDialog;
    SessionManager sessionManager;
    boolean flagForOtherScreen = false;
    String stockStorage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setData();
    }

    public void setData() {
        sessionManager = new SessionManager(MainActivity.this);
        showDialogAsset();
        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnUpdate.setOnClickListener(v -> {
            checkValidation();
        });
        binding.ivHistroy.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(i);
        });
    }

    public void showDialogAsset() {
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAssestNumberBinding dialogAssestNumberBinding = DataBindingUtil.inflate(LayoutInflater.from(MainActivity.this), R.layout.dialog_assest_number, null, false);
        dialog.setContentView(dialogAssestNumberBinding.getRoot());
        ColorDrawable back = new ColorDrawable(android.graphics.Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        dialog.getWindow().setBackgroundDrawable(inset);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dialogAssestNumberBinding.toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbWarehouse:
                        // do operations specific to this selection
                        stockStorage = "W";
                       // Toast.makeText(MainActivity.this, "DisbannedCoins", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rbStore:
                        // do operations specific to this selection
                        stockStorage = "S";
                       // Toast.makeText(MainActivity.this, "SwitchedCoins", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
        dialogAssestNumberBinding.btnsubmit.setOnClickListener(v -> {
            String userName = Objects.requireNonNull(dialogAssestNumberBinding.edtAssetNo.getText()).toString().trim();
            if (userName.isEmpty()) {
                dialogAssestNumberBinding.edtAssetNo.setError("Enter Asset Number");
                dialogAssestNumberBinding.edtAssetNo.requestFocus();
            } else if (dialogAssestNumberBinding.toggle.getCheckedRadioButtonId() == -1) {
                Toast.makeText(MainActivity.this, "Select Warehouse or Store", Toast.LENGTH_SHORT).show();
            } else {
                getAssetDetailData(dialogAssestNumberBinding.edtAssetNo.getText().toString(),stockStorage, dialogAssestNumberBinding);

            }
        });
        dialogAssestNumberBinding.btnHistroy.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(i);
        });

        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.e("flagForOtherScreen::", flagForOtherScreen + "");
                if (flagForOtherScreen) {
                    binding.constraintAssetDetails.setVisibility(View.VISIBLE);
                   /* Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();*/
                } else {
                    finish();
                }

            }
        });
       /* dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    finish();
                }
                return true;
            }
        });*/
    }

    /*    @Override
        public void onBackPressed() {
          // super.onBackPressed();
            if(dialog.isShowing())
            {
                finish();
            }

        }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showAlertDialog(String msg, int flag) {
        alertDialog = new Dialog(MainActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogYesNoBinding yesNoBinding = DataBindingUtil.inflate(LayoutInflater.from(MainActivity.this), R.layout.dialog_yes_no, null, false);
        alertDialog.setContentView(yesNoBinding.getRoot());
        ColorDrawable back = new ColorDrawable(android.graphics.Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        alertDialog.getWindow().setBackgroundDrawable(inset);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        yesNoBinding.tvMsg.setText(msg);
        yesNoBinding.tvYes.setOnClickListener(v -> {
            if (flag == 1) {
                alertDialog.dismiss();
            } else {
                alertDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
        yesNoBinding.tvNo.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    public void checkValidation() {
        String qty = Objects.requireNonNull(binding.edtQty.getText()).toString();
        if (qty.isEmpty()) {
            binding.edtQty.setError("Enter Physical Quantity");
            binding.edtQty.requestFocus();

        } else {
            updateQty(binding.edtQty.getText().toString(),binding.tvMainCategory.getText().toString());
        }
    }

    private void getAssetDetailData(String tagNumber,String stockStorage, DialogAssestNumberBinding bindingEdt) {
        if (!isInternetOn(MainActivity.this)) {
            showToast(getString(R.string.check_internet));
            return;
        }
        showCustomLoader(MainActivity.this);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Log.e("AssetDetails Data ::", "tagNumber:" + tagNumber);

        Call<RestResponseAssetDetials> callApi = apiInterface.getAssetDetails(tagNumber,stockStorage);
        callApi.enqueue(new Callback<RestResponseAssetDetials>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NotNull Call<RestResponseAssetDetials> call, @NotNull Response<RestResponseAssetDetials> response) {

                Log.e("AssetDetails_msg", "------" + response.message());
                Log.e("AssetDetails_code", "------" + response.code());
                if (response.body() != null) {
                    dismissCustomLoader();
                    Log.e("AssetDetailsData Response ::", new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        if (response.body().getResult().getStatus() == 1) {
                            bindingEdt.edtAssetNo.setText("");

                            showAlertDialog("Asset Already Audited", 1);

                        } else if (response.body().getResult().getStatus() == 0) {
                            flagForOtherScreen = true;
                            dialog.dismiss();

                            binding.constraintAssetDetails.setVisibility(View.VISIBLE);
                            // binding.rlAssetStatus.setVisibility(View.VISIBLE);
                            binding.tvTagNumber.setText(response.body().getResult().getTag_number());
                            binding.tvAssetName.setText(response.body().getResult().getAsset());
                            binding.tvActualQuantity.setText(response.body().getResult().getQty());
                            binding.tvAvailableQty.setText(response.body().getResult().getAvailable_qty());
                            binding.tvcategory.setText(response.body().getResult().getCategory());
                            binding.tvuom.setText(response.body().getResult().getUom());
                            binding.tvLocation.setText(response.body().getResult().getLocation());
                            binding.tvSubLocation.setText(response.body().getResult().getSub_location());
                            binding.tvItemCode.setText(response.body().getResult().getDate());
                            String type;
                            if (response.body().getResult().getMain_category().equals("W"))
                                type="Warehouse";
                            else
                                type="Store";
                            binding.tvMainCategory.setText(type);

                        }


                        // Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    dismissCustomLoader();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestResponseAssetDetials> call, @NotNull Throwable t) {
                dismissCustomLoader();
                Log.e("Error >> ", t.getMessage());
                Toast.makeText(MainActivity.this, "AssetDetails In Failed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQty(String qty,String storageType) {
        if (!isInternetOn(MainActivity.this)) {
            showToast(getString(R.string.check_internet));
            return;
        }
        String maincategoryType="";
        if(storageType.equals("Warehouse"))
            maincategoryType="W";
        else
            maincategoryType="S";


        showCustomLoader(MainActivity.this);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Log.e("QtyUpdate Data ::", "tagNumber:" + binding.edtQty.getText().toString() + "qty:" + qty);

        Call<RestResponseAssetDetials> callApi = apiInterface.updateQuantity(binding.tvTagNumber.getText().toString(), qty, "1",maincategoryType, String.valueOf(sessionManager.getLogin().getUser_id()));
        callApi.enqueue(new Callback<RestResponseAssetDetials>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NotNull Call<RestResponseAssetDetials> call, @NotNull Response<RestResponseAssetDetials> response) {

                Log.e("QtyUpdate_msg", "------" + response.message());
                Log.e("QtyUpdate_code", "------" + response.code());
                if (response.body() != null) {
                    dismissCustomLoader();
                    Log.e("QtyUpdateData Response ::", new Gson().toJson(response.body()));
                    if (response.body().getStatus() == 1) {
                        binding.edtQty.setText("");
                        showAlertDialog(response.body().getMessage(), 0);
                        //Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    dismissCustomLoader();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RestResponseAssetDetials> call, @NotNull Throwable t) {
                dismissCustomLoader();
                Log.e("Error >> ", t.getMessage());
                Toast.makeText(MainActivity.this, "QtyUpdate In Failed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}