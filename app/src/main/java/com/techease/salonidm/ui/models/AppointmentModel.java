package com.techease.salonidm.ui.models;

/**
 * Created by kaxhiftaj on 3/2/18.
 */

public class AppointmentModel {

    String merchant_id,client_name, service_name, booking_start_time, booking_end_time,appointment_status, appointment_type, price ,payment_status,booking_date ;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getBooking_start_time() {
        return booking_start_time;
    }

    public void setBooking_start_time(String booking_start_time) {
        this.booking_start_time = booking_start_time;
    }

    public String getBooking_end_time() {
        return booking_end_time;
    }

    public void setBooking_end_time(String booking_end_time) {
        this.booking_end_time = booking_end_time;
    }

    public String getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
    }

    public String getAppointment_type() {
        return appointment_type;
    }

    public void setAppointment_type(String appointment_type) {
        this.appointment_type = appointment_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

}
