package com.example.dealerapp.Utils;

public class Categories {

    private String item_category;
    private String item_id;

    public Categories(String item_category, String item_id) {
        this.item_category = item_category;
        this.item_id = item_id;
    }

    public Categories(){

    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
