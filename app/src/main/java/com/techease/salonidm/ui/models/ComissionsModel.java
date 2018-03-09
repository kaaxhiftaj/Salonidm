package com.techease.salonidm.ui.models;

/**
 * Created by kaxhiftaj on 3/9/18.
 */

public class ComissionsModel {

    String payment_method, total_amount, earned_amount, date ;

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getEarned_amount() {
        return earned_amount;
    }

    public void setEarned_amount(String earned_amount) {
        this.earned_amount = earned_amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
