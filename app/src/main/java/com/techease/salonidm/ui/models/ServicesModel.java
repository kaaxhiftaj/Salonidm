package com.techease.salonidm.ui.models;

/**
 * Created by kaxhiftaj on 2/21/18.
 */

public class ServicesModel {


    String service_price;
    String service_duration ;
    String service_name;
    String service_id ;
    String service_image ;
    String service_desc;
    String service_discount;

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }



    public String getService_price() {
        return service_price;
    }

    public void setService_price(String service_price) {
        this.service_price = service_price;
    }

    public String getService_duration() {
        return service_duration;
    }

    public void setService_duration(String service_duration) {
        this.service_duration = service_duration;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public void setService_desc(String service_desc) {
        this.service_desc = service_desc;
    }

    public String getService_desc() {
        return service_desc;
    }

    public void setService_discount(String service_discount) {
        this.service_discount = service_discount;
    }

    public String getService_discount() {
        return service_discount;
    }
}
