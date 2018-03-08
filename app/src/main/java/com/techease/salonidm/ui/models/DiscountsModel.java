package com.techease.salonidm.ui.models;

/**
 * Created by kaxhiftaj on 3/5/18.
 */

public class DiscountsModel {

     String merchant_id;
     String discount_code;
     String discount_percentage;
     String valid_from;
     String valid_to;
     String status;

     public String getDiscount_offer_id() {
          return discount_offer_id;
     }

     public void setDiscount_offer_id(String discount_offer_id) {
          this.discount_offer_id = discount_offer_id;
     }

     String discount_offer_id;

     public String getMerchant_id() {
          return merchant_id;
     }

     public void setMerchant_id(String merchant_id) {
          this.merchant_id = merchant_id;
     }

     public String getDiscount_code() {
          return discount_code;
     }

     public void setDiscount_code(String discount_code) {
          this.discount_code = discount_code;
     }

     public String getDiscount_percentage() {
          return discount_percentage;
     }

     public void setDiscount_percentage(String discount_percentage) {
          this.discount_percentage = discount_percentage;
     }

     public String getValid_from() {
          return valid_from;
     }

     public void setValid_from(String valid_from) {
          this.valid_from = valid_from;
     }

     public String getValid_to() {
          return valid_to;
     }

     public void setValid_to(String valid_to) {
          this.valid_to = valid_to;
     }

     public String getStatus() {
          return status;
     }

     public void setStatus(String status) {
          this.status = status;
     }



}

