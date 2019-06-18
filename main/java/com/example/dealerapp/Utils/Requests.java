package com.example.dealerapp.Utils;

public class Requests {

    private String address1;
    private String address2;
    private String dealer_id;
    private String mob;
    private String profile_image;
    private String user_name;
    private String user_email;

    public Requests(String address1, String address2, String dealer_id,
                    String mob, String profile_image, String user_name, String user_email) {
        this.address1 = address1;
        this.address2 = address2;
        this.dealer_id = dealer_id;
        this.mob = mob;
        this.profile_image = profile_image;
        this.user_name = user_name;
        this.user_email = user_email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
