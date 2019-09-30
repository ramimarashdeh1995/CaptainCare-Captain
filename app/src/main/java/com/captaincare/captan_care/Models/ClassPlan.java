package com.captaincare.captan_care.Models;

public class ClassPlan {

    private String plan_id;
    private String plan_name_en;
    private String plan_name_ar;
    private String plan_logo;
    private String plan_period;
    private String plan_price;
    private String RPR;
    private String APR;
    private String push1;
    private String push2;
    private String push3;
    private String push4;
    private String push5;
    private String push6;
    private String free1;
    private String free2;
    private String free3;
    private String free4;
    private String free5;
    private String free6;



    // LOG IN VENDOR
    public ClassPlan(String plan_id, String plan_name_en, String plan_name_ar, String plan_logo, String plan_period, String plan_price, String RPR, String APR, String push1, String push2, String push3, String push4, String push5, String push6, String free1, String free2, String free3, String free4, String free5, String free6) {
        this.plan_id = plan_id;
        this.plan_name_en = plan_name_en;
        this.plan_name_ar = plan_name_ar;
        this.plan_logo = plan_logo;
        this.plan_period = plan_period;
        this.plan_price = plan_price;
        this.RPR = RPR;
        this.APR = APR;
        this.push1 = push1;
        this.push2 = push2;
        this.push3 = push3;
        this.push4 = push4;
        this.push5 = push5;
        this.push6 = push6;
        this.free1 = free1;
        this.free2 = free2;
        this.free3 = free3;
        this.free4 = free4;
        this.free5 = free5;
        this.free6 = free6;
    }

    // LOG IN CAPTAIN

    public ClassPlan(String plan_id, String plan_name_en, String plan_name_ar, String plan_logo, String plan_period, String plan_price, String RPR, String APR, String push1, String push2, String push3, String push4, String free1, String free2, String free3, String free4) {
        this.plan_id = plan_id;
        this.plan_name_en = plan_name_en;
        this.plan_name_ar = plan_name_ar;
        this.plan_logo = plan_logo;
        this.plan_period = plan_period;
        this.plan_price = plan_price;
        this.RPR = RPR;
        this.APR = APR;
        this.push1 = push1;
        this.push2 = push2;
        this.push3 = push3;
        this.push4 = push4;
        this.free1 = free1;
        this.free2 = free2;
        this.free3 = free3;
        this.free4 = free4;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public String getPlan_name_en() {
        return plan_name_en;
    }

    public String getPlan_name_ar() {
        return plan_name_ar;
    }

    public String getPlan_logo() {
        return plan_logo;
    }

    public String getPlan_period() {
        return plan_period;
    }

    public String getPlan_price() {
        return plan_price;
    }

    public String getRPR() {
        return RPR;
    }

    public String getAPR() {
        return APR;
    }

    public String getPush1() {
        return push1;
    }

    public String getPush2() {
        return push2;
    }

    public String getPush3() {
        return push3;
    }

    public String getPush4() {
        return push4;
    }

    public String getPush5() {
        return push5;
    }

    public String getPush6() {
        return push6;
    }

    public String getFree1() {
        return free1;
    }

    public String getFree2() {
        return free2;
    }

    public String getFree3() {
        return free3;
    }

    public String getFree4() {
        return free4;
    }

    public String getFree5() {
        return free5;
    }

    public String getFree6() {
        return free6;
    }
}
