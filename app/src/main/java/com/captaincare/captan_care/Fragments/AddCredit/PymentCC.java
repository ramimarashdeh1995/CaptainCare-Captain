package com.captaincare.captan_care.Fragments.AddCredit;

public class PymentCC {
    private String P_ID;
    private String P_CC;
    private String P_Price;

    public PymentCC(String p_ID, String p_CC, String p_Price) {
        P_ID = p_ID;
        P_CC = p_CC;
        P_Price = p_Price;
    }

    public String getP_ID() {
        return P_ID;
    }

    public String getP_CC() {
        return P_CC;
    }

    public String getP_Price() {
        return P_Price;
    }
}
