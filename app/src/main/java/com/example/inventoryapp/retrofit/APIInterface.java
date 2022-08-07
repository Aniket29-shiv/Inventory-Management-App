package com.example.inventoryapp.retrofit;


import com.example.inventoryapp.retrofit.response.RestResponse;
import com.example.inventoryapp.retrofit.response.RestResponseAssetDetials;
import com.example.inventoryapp.retrofit.response.RestResponseAssetDetialsList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("app_login")
    Call<RestResponse> login(@Field("email") String email,
                             @Field("password") String password);

    @FormUrlEncoded
    @POST("app_get_asset_details")
    Call<RestResponseAssetDetials> getAssetDetails(@Field("tag_number") String tag_number,
                                                   @Field("main_category") String main_category);

    @FormUrlEncoded
    @POST("app_update_quantity")
    Call<RestResponseAssetDetials> updateQuantity(@Field("tag_number") String tag_number,
                                                  @Field("available_qty") String available_qty,
                                                  @Field("status") String status,
                                                  @Field("main_category") String main_category,
                                                  @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("app_user_update_quantity_history")
    Call<RestResponseAssetDetialsList> updateQuantityHistory(@Field("user_id") String user_id);


}
