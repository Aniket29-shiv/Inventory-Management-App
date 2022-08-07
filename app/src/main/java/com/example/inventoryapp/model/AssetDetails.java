package com.example.inventoryapp.model;

public class AssetDetails {


    /**
     * table_id : 1
     * user_id : 1
     * product_id : 983
     * available_qty : 10
     * tag_number : 1002
     * date :
     * location : SHED
     * sub_location : 11,B-7
     * asset : PARKING BRAKE RELEASE LEVER ASSEMBLY MODIFIED
     * category :
     * qty : 56
     * uom :
     * status : 1
     */

    private int table_id;
    private String user_id;
    private String product_id;
    private String available_qty;
    private String tag_number;
    private String date;
    private String location;
    private String sub_location;
    private String asset;
    private String category;

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    private String main_category;
    private String qty;
    private String uom;
    private int status;

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }

    public String getTag_number() {
        return tag_number;
    }

    public void setTag_number(String tag_number) {
        this.tag_number = tag_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSub_location() {
        return sub_location;
    }

    public void setSub_location(String sub_location) {
        this.sub_location = sub_location;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
