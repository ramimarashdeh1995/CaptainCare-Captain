package com.captaincare.captan_care.Models;

public class FollowAndBlockClass {

    String userId;
    String userName;
    String userMobile;
    String userCity;
    String userPhoto;
    String userToken;
    String blockId;
    String venAddress;
    String venLon;
    String venLat;
    String isFollow;


    public FollowAndBlockClass(String userId, String userName, String userPhoto, String userToken, String blockId) {
        this.userId = userId;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userToken = userToken;
        this.blockId = blockId;
    }

    public FollowAndBlockClass(String userId, String userName, String userMobile, String userCity, String userPhoto, String userToken, String isfollow) {
        this.userId = userId;
        this.userName = userName;
        this.userMobile = userMobile;
        this.userCity = userCity;
        this.userPhoto = userPhoto;
        this.userToken = userToken;
        this.isFollow=isfollow;
    }

    public FollowAndBlockClass(String userId, String userName, String userMobile, String userCity, String userPhoto, String venAddress, String venLon, String venLat, String userToken, String isFollow) {
        this.userId = userId;
        this.userName = userName;
        this.userMobile = userMobile;
        this.userCity = userCity;
        this.userPhoto = userPhoto;
        this.venAddress = venAddress;
        this.venLon = venLon;
        this.venLat = venLat;
        this.userToken = userToken;
        this.isFollow=isFollow;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getVenAddress() {
        return venAddress;
    }

    public String getVenLon() {
        return venLon;
    }
    public String getUserToken() {
        return userToken;
    }

    public String getBlockId() {
        return blockId;
    }
    public String getVenLat() {
        return venLat;
    }

    public String getIsFollow() {
        return isFollow;
    }
}
