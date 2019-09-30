package com.captaincare.captan_care.Models;

public class VendorServiceClass {

    String subCategoryID;
    String categoryId;
    String subcategoryName_Ar;
    String subcategoryName_En;

    public VendorServiceClass(String subCategoryID, String categoryId, String subcategoryName_Ar, String subcategoryName_En) {
        this.subCategoryID = subCategoryID;
        this.categoryId = categoryId;
        this.subcategoryName_Ar = subcategoryName_Ar;
        this.subcategoryName_En = subcategoryName_En;
    }

    public String getSubCategoryID() {
        return subCategoryID;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getSubcategoryName_Ar() {
        return subcategoryName_Ar;
    }

    public String getSubcategoryName_En() {
        return subcategoryName_En;
    }
}
