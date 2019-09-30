package com.captaincare.captan_care.Models;

public class NotificationClass {

    private final String imag;
    private final String name;
    private final String title;
    private final String offerid;
    private final String ven_id;
    private final String sub_id;
    private final String city_offer;
    private final String cost;
    private final String type;
    private final String city;
    private final String lat;
    private final String lon;
    private final String disc;
    private final String address;
    private String time;
    private String DateNotification;
    private String isEnd;

    public String getIsEnd() {
        return isEnd;
    }

    public NotificationClass(String imag, String name, String title, String offerid, String ven_id, String sub_id, String city_offer, String cost, String type, String city, String lat, String lon, String disc, String address, String time, String dateNotification, String isEnd) {

        this.imag = imag;
        this.name = name;
        this.title = title;
        this.offerid = offerid;
        this.ven_id = ven_id;
        this.sub_id = sub_id;
        this.city_offer = city_offer;
        this.cost = cost;
        this.type = type;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.disc = disc;
        this.address = address;
        this.time = time;
        this.DateNotification=dateNotification;
        this.isEnd = isEnd;
    }



    public String getOfferid() {
        return offerid;
    }

    public String getVen_id() {
        return ven_id;
    }

    public String getSub_id() {
        return sub_id;
    }

    public String getCity_offer() {
        return city_offer;
    }

    public String getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getCity() {
        return city;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getDisc() {
        return disc;
    }

    public String getAddress() {
        return address;
    }
    public String getTime() {
        return time;
    }

    public String getImag() {
        return imag;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDateNotification(){return DateNotification;}
}
