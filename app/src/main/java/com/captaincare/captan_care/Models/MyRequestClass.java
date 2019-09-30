package com.captaincare.captan_care.Models;

public class MyRequestClass {

    private String offerId;
    private String userId;
    private String subCategoryId;
    private String categoryId;
    private String subcategoryName_Ar;
    private String subcategoryName_En;
    private String city;
    private String offerTitle;
    private String offerDescription;
    private String imgUrl1;
    private String imgUrl2;
    private String imgUrl3;
    private String imgUrl4;
    private String offerStartDate;
    private String offerEndDate;
    private String isEnd;
    private String cost;
    private String Address;

    public MyRequestClass(String offerId, String userId, String subCategoryId,
                          String categoryId, String subcategoryName_En, String subcategoryName_Ar, String city,
                          String offerTitle, String offerDescription, String imgUrl1, String imgUrl2, String imgUrl3,
                          String imgUrl4, String offerStartDate, String offerEndDate, String isEnd,
                          String cost, String Address) {
        this.offerId = offerId;
        this.userId = userId;
        this.subCategoryId = subCategoryId;
        this.categoryId = categoryId;
        this.subcategoryName_Ar = subcategoryName_Ar;
        this.subcategoryName_En = subcategoryName_En;
        this.city = city;
        this.offerTitle = offerTitle;
        this.offerDescription = offerDescription;
        this.imgUrl1 = imgUrl1;
        this.imgUrl2 = imgUrl2;
        this.imgUrl3 = imgUrl3;
        this.imgUrl4 = imgUrl4;
        this.offerStartDate = offerStartDate;
        this.offerEndDate = offerEndDate;
        this.isEnd = isEnd;
        this.cost=cost;
        this.Address=Address;
    }


    public String getOfferId() {
        return offerId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public String getSubcategoryName_Ar() {
        return subcategoryName_Ar;
    }

    public String getSubcategoryName_En() {
        return subcategoryName_En;
    }

    public String getCity() {
        return city;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public String getImgUrl4() {
        return imgUrl4;
    }

    public String getOfferStartDate() {
        return offerStartDate;
    }

    public String getOfferEndDate() {
        return offerEndDate;
    }

    public String getIsEnd() {
        return isEnd;
    }

    public String getCost() {
        return cost;
    }

    public String getAddress() {
        return Address;
    }
}
