package com.aggregator.BE;

/**
 * Created by Pranav Mittal on 9/11/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class TicketScreenBE {

    private String pickPointName;
    private Double pickPointLat;
    private Double pickPointLong;
    private String pickPointImage;

    private String dropPointName;
    private Double dropPointLat;
    private Double dropPointLong;

    private String vehicleType;
    private String vehicleRegistration;

    private String departureTime;

    public String getPickPointName() {
        return pickPointName;
    }

    public void setPickPointName(String pickPointName) {
        this.pickPointName = pickPointName;
    }

    public Double getPickPointLat() {
        return pickPointLat;
    }

    public void setPickPointLat(Double pickPointLat) {
        this.pickPointLat = pickPointLat;
    }

    public Double getPickPointLong() {
        return pickPointLong;
    }

    public void setPickPointLong(Double pickPointLong) {
        this.pickPointLong = pickPointLong;
    }

    public String getPickPointImage() {
        return pickPointImage;
    }

    public void setPickPointImage(String pickPointImage) {
        this.pickPointImage = pickPointImage;
    }

    public String getDropPointName() {
        return dropPointName;
    }

    public void setDropPointName(String dropPointName) {
        this.dropPointName = dropPointName;
    }

    public Double getDropPointLat() {
        return dropPointLat;
    }

    public void setDropPointLat(Double dropPointLat) {
        this.dropPointLat = dropPointLat;
    }

    public Double getDropPointLong() {
        return dropPointLong;
    }

    public void setDropPointLong(Double dropPointLong) {
        this.dropPointLong = dropPointLong;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}
