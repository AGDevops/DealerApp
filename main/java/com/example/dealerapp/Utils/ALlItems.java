package com.example.dealerapp.Utils;

public class ALlItems {

    private String item_image;
    private String item_brand;
    private String item_name;
    private String item_price;
    private String item_mrp;
    private String item_desc;
    private String item_discount;
    private String item_quantity;
    private String item_category;

    public ALlItems(String item_image, String item_brand, String item_name,
                    String item_price, String item_mrp, String item_desc,
                    String item_discount, String item_quantity, String item_category) {
        this.item_image = item_image;
        this.item_brand = item_brand;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_mrp = item_mrp;
        this.item_desc = item_desc;
        this.item_discount = item_discount;
        this.item_quantity = item_quantity;
        this.item_category = item_category;
    }

    public ALlItems(){

    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_brand() {
        return item_brand;
    }

    public void setItem_brand(String item_brand) {
        this.item_brand = item_brand;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_mrp() {
        return item_mrp;
    }

    public void setItem_mrp(String item_mrp) {
        this.item_mrp = item_mrp;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_discount() {
        return item_discount;
    }

    public void setItem_discount(String item_discount) {
        this.item_discount = item_discount;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }
}
