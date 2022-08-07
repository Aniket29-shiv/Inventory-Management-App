package com.example.inventoryapp.retrofit.response;
import com.example.inventoryapp.model.User;

import java.util.List;

public class RestResponse {

    private int status;
    private String message;
    private User result;
    public User getResult() {
        return result;
    }

    public void setResult(User result) {
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
