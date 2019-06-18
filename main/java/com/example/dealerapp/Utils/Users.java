package com.example.dealerapp.Utils;

public class Users {

    private String address1;
    private String address2;
    private String company;
    private String first_name;
    private String last_name;
    private String mob;
    private String profile_pic;
    private String profile_thumbUrl;
    private String type;
    private String user_email;
    private String user_name;
    private String uId;

    public Users(String address1, String address2, String company, String first_name, String last_name, String mob, String profile_pic,
                 String profile_thumbUrl, String type, String user_email, String user_name,
                 String uId) {
        this.address1 = address1;
        this.address2 = address2;
        this.company = company;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mob = mob;
        this.profile_pic = profile_pic;
        this.profile_thumbUrl = profile_thumbUrl;
        this.type = type;
        this.user_email = user_email;
        this.user_name = user_name;
        this.uId = uId;
    }

    public Users(){

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getProfile_thumbUrl() {
        return profile_thumbUrl;
    }

    public void setProfile_thumbUrl(String profile_thumbUrl) {
        this.profile_thumbUrl = profile_thumbUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
