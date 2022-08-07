package com.example.inventoryapp.retrofit.response;
import com.example.inventoryapp.model.AssetDetails;
import com.example.inventoryapp.model.User;

public class RestResponseAssetDetials {

    private int status;
    private String message;

    public AssetDetails getResult() {
        return result;
    }

    public void setResult(AssetDetails result) {
        this.result = result;
    }

    private AssetDetails result;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }






}
