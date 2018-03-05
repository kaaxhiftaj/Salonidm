package com.techease.salonidm.ui.models;

/**
 * Created by kaxhiftaj on 2/28/18.
 */

public class BusinessHoursModel {

     String day;
    String start_time;
    String end_time;
    String check_day;
    String break_day;
    String break_start_time;
    String break_end_time ;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCheck_day() {
        return check_day;
    }

    public void setCheck_day(String check_day) {
        this.check_day = check_day;
    }

    public String getBreak_day() {
        return break_day;
    }

    public void setBreak_day(String break_day) {
        this.break_day = break_day;
    }

    public String getBreak_start_time() {
        return break_start_time;
    }

    public void setBreak_start_time(String break_start_time) {
        this.break_start_time = break_start_time;
    }

    public String getBreak_end_time() {
        return break_end_time;
    }

    public void setBreak_end_time(String break_end_time) {
        this.break_end_time = break_end_time;
    }


}
