package com.example.ruokapp.Model;

public class BookingInformation {

    private String userName, userPhone, time, therapistId, therapistName, preferencesId, preferencesName, preferencesAddress;
    private Long slot;

    public BookingInformation() {
    }

    public BookingInformation(String userName, String userPhone, String time, String therapistId, String therapistName, String preferencesId, String preferencesName, String preferencesAddress, Long slot) {
        this.userName = userName;
        this.userPhone = userPhone;
        this.time = time;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.preferencesId = preferencesId;
        this.preferencesName = preferencesName;
        this.preferencesAddress = preferencesAddress;
        this.slot = slot;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    public String getTherapistName() {
        return therapistName;
    }

    public void setTherapistName(String therapistName) {
        this.therapistName = therapistName;
    }

    public String getPreferencesId() {
        return preferencesId;
    }

    public void setPreferencesId(String preferencesId) {
        this.preferencesId = preferencesId;
    }

    public String getPreferencesName() {
        return preferencesName;
    }

    public void setPreferencesName(String preferencesName) {
        this.preferencesName = preferencesName;
    }

    public String getPreferencesAddress() {
        return preferencesAddress;
    }

    public void setPreferencesAddress(String preferencesAddress) {
        this.preferencesAddress = preferencesAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
