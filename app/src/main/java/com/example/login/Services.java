package com.example.login;

import java.util.Calendar;
import java.util.Date;

public class Services {


    private int batteryLevel = 0;
    private int deviceVolume = 0;

    @Override
    public String toString() {
        return "Services{" +
                "batteryLevel=" + batteryLevel +
                ", deviceVolume=" + deviceVolume +
                ", loginLocation='" + loginLocation + '\'' +
                ", dayAndHour=" + dayAndHour +
                ", ipDevice='" + ipDevice + '\'' +
                '}';
    }

    private String loginLocation = "";
    private Date dayAndHour;
    private String ipDevice="";

    public Services(int batteryLevel, int deviceVolume, String loginLocation, String ipDevice) {
        this.batteryLevel = batteryLevel;
        this.deviceVolume = deviceVolume;
        this.loginLocation = loginLocation;
        this.ipDevice = ipDevice;
    }

    public Services() {
        dayAndHour =new Date();
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getDeviceVolume() {
        return deviceVolume;
    }

    public void setDeviceVolume(int deviceVolume) {
        this.deviceVolume = deviceVolume;
    }

    public String getLoginLocation() {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getDayAndHour() {

        return dayAndHour.toString();

    }

    public void setDayAndHour(Date dayAndHour) {
        this.dayAndHour = dayAndHour;
    }

    public String getIpDevice() {
        return ipDevice;
    }

    public void setIpDevice(String ipDevice) {
        this.ipDevice = ipDevice;
    }
}
