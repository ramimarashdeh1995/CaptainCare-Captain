package com.captaincare.captan_care.Models;

public class CurrentOfferClass {
    private String UserID;
    private String UserName;
    private String UserImageProfile;
    private String UserMobile;
    private String SaveID;
    private String Rank;

    private String OfferIDCaptain,OfferIDVendor;
    private String Lon,Lat;
    private String pic1;
    private String pic2;
    private String pic3;
    private String pic4;
    private String title;
    private String disc;
    private String enddate;
    private String Cost_ven,Cost_cap;

    String city_ven,city_cap;

    private String Cappic1;
    private String Cappic2;
    private String Cappic3;
    private String Cappic4;
    private String Captitle;
    private String Capdisc;
    private String Capenddate;

    private String isEnd;


    public CurrentOfferClass(String userID, String userName, String userImageProfile, String userMobile,
                             String saveID, String rank, String offerIDCaptain, String offerIDVendor, String lon, String lat,
                             String pic1, String pic2, String pic3, String pic4, String title, String disc, String enddate,
                             String cappic1, String cappic2, String cappic3, String cappic4, String captitle, String capdisc,
                             String capenddate, String isEnd, String Cost, String Cost2,
                             String cityVen, String cityCap) {
        this.UserID = userID;
        this.UserName = userName;
        this.UserImageProfile = userImageProfile;
        this.UserMobile = userMobile;
        this.SaveID = saveID;
        this.Rank = rank;
        this.OfferIDCaptain = offerIDCaptain;
        this.OfferIDVendor = offerIDVendor;
        this.Lon = lon;
        this.Lat = lat;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.pic4 = pic4;
        this.title = title;
        this.disc = disc;
        this.enddate = enddate;
        this.Cappic1 = cappic1;
        this.Cappic2 = cappic2;
        this.Cappic3 = cappic3;
        this.Cappic4 = cappic4;
        this.Captitle = captitle;
        this.Capdisc = capdisc;
        this.Capenddate = capenddate;
        this.isEnd=isEnd;
        this.Cost_ven=Cost;
        this.Cost_cap=Cost2;
        this.city_ven=cityVen;
        this.city_cap=cityCap;
    }

    public String getUserID() {
        return UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserImageProfile() {
        return UserImageProfile;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public String getSaveID() {
        return SaveID;
    }

    public String getRank() {
        return Rank;
    }

    public String getLon() {
        return Lon;
    }

    public String getLat() {
        return Lat;
    }

    public String getOfferIDCaptain() {
        return OfferIDCaptain;
    }

    public String getOfferIDVendor() {
        return OfferIDVendor;
    }

    public String getPic1() {
        return pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public String getPic4() {
        return pic4;
    }

    public String getTitle() {
        return title;
    }

    public String getDisc() {
        return disc;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getCappic1() {
        return Cappic1;
    }

    public String getCappic2() {
        return Cappic2;
    }

    public String getCappic3() {
        return Cappic3;
    }

    public String getCappic4() {
        return Cappic4;
    }

    public String getCaptitle() {
        return Captitle;
    }

    public String getCapdisc() {
        return Capdisc;
    }

    public String getCapenddate() {
        return Capenddate;
    }

    public String getIsEnd(){
        return isEnd;
    }

    public String getCostven() {
        return Cost_ven;
    }

    public String getCostcap() {
        return Cost_cap;
    }

    public String getCity_ven() {
        return city_ven;
    }

    public String getCity_cap() {
        return city_cap;
    }
}
