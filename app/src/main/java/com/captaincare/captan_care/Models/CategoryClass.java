package com.captaincare.captan_care.Models;

public class CategoryClass {

    String categoryId;
    String categoryOmgUrl;
    String categoryName_Ar;
    String categoryName_En;
    String categoryimgBackground;

    public CategoryClass(String categoryId, String categoryName_Ar, String categoryName_En, String categoryimgUrl, String categoryimgBackground) {
        this.categoryId = categoryId;
        this.categoryName_Ar = categoryName_Ar;
        this.categoryName_En = categoryName_En;
        this.categoryOmgUrl = categoryimgUrl;
        this.categoryimgBackground = categoryimgBackground;
    }
    public CategoryClass(String categoryId, String categoryName_Ar, String categoryName_En) {
        this.categoryId = categoryId;
        this.categoryName_Ar = categoryName_Ar;
        this.categoryName_En = categoryName_En;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryimgUrl() {
        return categoryOmgUrl;
    }

    public String getCategoryName_Ar() {
        return categoryName_Ar;
    }

    public String getCategoryName_En() {
        return categoryName_En;
    }

    public String getCategoryimgBackground() {
        return categoryimgBackground;
    }


}
