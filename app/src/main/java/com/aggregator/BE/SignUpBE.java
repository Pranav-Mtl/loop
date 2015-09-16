package com.aggregator.BE;

/**
 * Created by Pranav Mittal on 9/1/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class SignUpBE {

    //Http://www.appslure.in/loop/webservices/signup.php?firstname=Gaurav%20Gupta&email=gaurav@gmail.com&password=welcome&phone_no=9412132809&paytm=&promo=&otp=8513

    private String name;
    private String email;
    private String password;
    private String phone;
    private String payTm;
    private String promo;
    private String Otp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayTm() {
        return payTm;
    }

    public void setPayTm(String payTm) {
        this.payTm = payTm;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        Otp = otp;
    }
}
