package com.example.dealerapp.Utils;

public class Product {
    public Product(String item_brand, String item_category, String item_desc, long item_discount,
                   String item_image, long item_mrp, String item_name,
                   long item_price, long item_quantity, String item_id) {
        this.item_brand = item_brand;
        this.item_category = item_category;
        this.item_desc = item_desc;
        this.item_discount = item_discount;
        this.item_image = item_image;
        this.item_mrp = item_mrp;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_quantity = item_quantity;
        this.item_id = item_id;
    }

    public Product()
    {

    }
    String item_brand;
    String item_category;
    String item_desc;
    long item_discount;

    public long getItem_discount() {
        return item_discount;
    }

    public void setItem_discount(long item_discount) {
        this.item_discount = item_discount;
    }

    public long getItem_mrp() {
        return item_mrp;
    }

    public void setItem_mrp(long item_mrp) {
        this.item_mrp = item_mrp;
    }

    public long getItem_price() {
        return item_price;
    }

    public void setItem_price(long item_price) {
        this.item_price = item_price;
    }

    public long getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(long item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_brand() {
        return item_brand;
    }

    public void setItem_brand(String item_brand) {
        this.item_brand = item_brand;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    String item_image;
    long item_mrp;
    String item_name;
    long item_price;
    long item_quantity;
    String item_id;
}
