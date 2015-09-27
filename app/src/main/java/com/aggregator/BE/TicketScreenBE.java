package com.aggregator.BE;

import java.io.Serializable;

/**
 * Created by Pranav Mittal on 9/11/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class TicketScreenBE implements Serializable {

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

    private Double startPointLat;
    private Double endPointLong;
    private Double startPointLong;
    private Double endPointLat;

    public Double getStartPointLong() {
        return startPointLong;
    }

    public void setStartPointLong(Double startPointLong) {
        this.startPointLong = startPointLong;
    }

    public Double getEndPointLat() {
        return endPointLat;
    }

    public void setEndPointLat(Double endPointLat) {
        this.endPointLat = endPointLat;
    }

    public String getPickPointName() {
        return pickPointName;
    }

    public Double getStartPointLat() {
        return startPointLat;
    }

    public void setStartPointLat(Double startPointLat) {
        this.startPointLat = startPointLat;
    }

    public Double getEndPointLong() {
        return endPointLong;
    }

    public void setEndPointLong(Double endPointLong) {
        this.endPointLong = endPointLong;
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
