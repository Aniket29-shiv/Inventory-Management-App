package com.example.inventoryapp.retrofit.response;
import com.example.inventoryapp.model.AssetDetails;

import java.util.ArrayList;

public class RestResponseAssetDetialsList {

    private int status;
    private String message;
    private ArrayList<AssetDetails> result;
    public ArrayList<AssetDetails> getResult() {
        return result;
    }

    public void setResult(ArrayList<AssetDetails> result) {
        this.result = result;
    }
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
