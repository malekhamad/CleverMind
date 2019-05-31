package com.geniusmind.malek.clevermind.Model;

public class UserInformation {
    private String fullname;
    private String username;
    private String email;
    private String phone;
    private String password;
    private String mDate;
    private String id;

    public UserInformation( String fullname, String username, String email, String phone, String password, String date) {

        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        mDate = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId(){return id;}

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getDate() {
        return mDate;
    }

    public void setEmail(String email){
        this.email=email;
    }
}
