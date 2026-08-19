package com.zybooks.cs360inventoryapp.models;

public class SMS {
    String phoneNumber;
    boolean toggle;

    public SMS(String phoneNumber, boolean toggle) {
        this.phoneNumber = phoneNumber;
        this.toggle = toggle;
    }

    public SMS() {
        this.phoneNumber = "";
        this.toggle = false;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isToggle() {
        return toggle;
    }
}
