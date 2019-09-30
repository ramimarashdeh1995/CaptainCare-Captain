package com.captaincare.captan_care.Models;

public class OfferClass {

    private String offer_id;
    private String user_id;
    private String sub_id;
    private String city;
    private String offer_title;
    private String offer_disc;
    private String offer_pic1;
    private String offer_pic2;
    private String offer_pic3;
    private String offer_pic4;
    private String offer_pic5;
    private String offer_pic6;
    private String offer_start;
    private String offer_end;
    private String isEnd;
    private String user_name;
    private String user_mobile;
    private String user_city;
    private String vendor_address;
    private String user_photo_url;
    private String user_token;
    private String IsFavorite;
    private String lon,lat;

    private String ispaid,cost,eval;

    public OfferClass(String offer_id, String user_id, String sub_id, String city, String offer_title,
                      String offer_disc, String offer_pic1, String offer_pic2, String offer_pic3, String offer_pic4,
                      String offer_pic5, String offer_pic6, String offer_start, String offer_end, String isEnd,
                      String user_name, String user_mobile, String user_city, String user_photo_url,
                      String user_token, String isfav, String ispaid, String cost, String address,
                      String eval) {
        this.offer_id = offer_id;
        this.user_id = user_id;
        this.sub_id = sub_id;
        this.city = city;
        this.offer_title = offer_title;
        this.offer_disc = offer_disc;
        this.offer_pic1 = offer_pic1;
        this.offer_pic2 = offer_pic2;
        this.offer_pic3 = offer_pic3;
        this.offer_pic4 = offer_pic4;
        this.offer_pic5 = offer_pic5;
        this.offer_pic6 = offer_pic6;
        this.offer_start = offer_start;
        this.offer_end = offer_end;
        this.isEnd = isEnd;
        this.user_name = user_name;
        this.user_mobile = user_mobile;
        this.user_city = user_city;
        this.user_photo_url = user_photo_url;
        this.user_token = user_token;
        this.IsFavorite=isfav;
        this.ispaid=ispaid;
        this.cost=cost;
        this.eval=eval;
        this.vendor_address=address;
    }
    public OfferClass(String offer_id, String user_id, String sub_id, String city, String offer_title,
                      String offer_disc, String offer_pic1, String offer_pic2, String offer_pic3, String offer_pic4,
                      String offer_pic5, String offer_pic6, String offer_start, String offer_end, String isEnd, String user_name,
                      String user_mobile, String user_city, String user_photo_url, String user_token, String vendor_address,
                      String isfav , String venlon, String venlat, String ispaid, String cost, String eval) {
        this.offer_id = offer_id;
        this.user_id = user_id;
        this.sub_id = sub_id;
        this.city = city;
        this.offer_title = offer_title;
        this.offer_disc = offer_disc;
        this.offer_pic1 = offer_pic1;
        this.offer_pic2 = offer_pic2;
        this.offer_pic3 = offer_pic3;
        this.offer_pic4 = offer_pic4;
        this.offer_pic5 = offer_pic5;
        this.offer_pic6 = offer_pic6;
        this.offer_start = offer_start;
        this.offer_end = offer_end;
        this.isEnd = isEnd;
        this.user_name = user_name;
        this.user_mobile = user_mobile;
        this.user_city = user_city;
        this.user_photo_url = user_photo_url;
        this.user_token = user_token;
        this.vendor_address=vendor_address;
        this.IsFavorite=isfav;
        this.lon=venlon;
        this.lat=venlat;
        this.ispaid=ispaid;
        this.cost=cost;
        this.eval=eval;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSub_id() {
        return sub_id;
    }

    public String getCity() {
        return city;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public String getOffer_disc() {
        return offer_disc;
    }

    public String getOffer_pic1() {
        return offer_pic1;
    }

    public String getOffer_pic2() {
        return offer_pic2;
    }

    public String getOffer_pic3() {
        return offer_pic3;
    }

    public String getOffer_pic4() {
        return offer_pic4;
    }

    public String getOffer_pic5() {
        return offer_pic5;
    }

    public String getOffer_pic6() {
        return offer_pic6;
    }

    public String getOffer_start() {
        return offer_start;
    }

    public String getOffer_end() {
        return offer_end;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public String getUser_city() {
        return user_city;
    }

    public String getUser_photo_url() {
        return user_photo_url;
    }

    public String getUser_token() {
        return user_token;
    }

    public String getIs_Favorite(){
        return IsFavorite;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getVendor_address(){
        return vendor_address;
    }

    public String getIspaid() {
        return ispaid;
    }

    public String getCost() {
        return cost;
    }

    public String getEval() {
        return eval;
    }
}
