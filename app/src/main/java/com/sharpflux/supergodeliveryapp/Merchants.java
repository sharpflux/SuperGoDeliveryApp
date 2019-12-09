package com.sharpflux.supergodeliveryapp;

public class Merchants {
    public String MerchantId;
    public String ImageUrl;
    public String TypeName;
    public String FirmName;
    public String mobileNum,FromLat,FromLong,MerchantAddress;
    public String TotalCharges;
    public String GstAmount;
    public String Kilmoter;

    public Merchants(String merchantId, String imageUrl, String typeName, String firmName, String mobileNum, String fromLat, String fromLong, String merchantAddress) {
        MerchantId = merchantId;
        ImageUrl = imageUrl;
        TypeName = typeName;
        FirmName = firmName;
        this.mobileNum = mobileNum;
        FromLat = fromLat;
        FromLong = fromLong;
        MerchantAddress = merchantAddress;
    }

    public Merchants(String merchantId, String imageUrl, String typeName, String firmName, String mobileNum, String fromLat, String fromLong, String merchantAddress, String totalCharges, String gstAmount) {
        MerchantId = merchantId;
        ImageUrl = imageUrl;
        TypeName = typeName;
        FirmName = firmName;
        this.mobileNum = mobileNum;
        FromLat = fromLat;
        FromLong = fromLong;
        MerchantAddress = merchantAddress;
        TotalCharges = totalCharges;
        GstAmount = gstAmount;
    }

    public Merchants(String merchantId, String imageUrl, String typeName, String firmName, String mobileNum, String fromLat, String fromLong, String merchantAddress, String totalCharges, String gstAmount, String kilmoter) {
        MerchantId = merchantId;
        ImageUrl = imageUrl;
        TypeName = typeName;
        FirmName = firmName;
        this.mobileNum = mobileNum;
        FromLat = fromLat;
        FromLong = fromLong;
        MerchantAddress = merchantAddress;
        TotalCharges = totalCharges;
        GstAmount = gstAmount;
        Kilmoter = kilmoter;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public String getFromLat() {
        return FromLat;
    }

    public void setFromLat(String fromLat) {
        FromLat = fromLat;
    }

    public String getFromLong() {
        return FromLong;
    }

    public void setFromLong(String fromLong) {
        FromLong = fromLong;
    }

    public String getMerchantAddress() {
        return MerchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        MerchantAddress = merchantAddress;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getTotalCharges() {
        return TotalCharges;
    }

    public String getKilmoter() {
        return Kilmoter;
    }

    public void setKilmoter(String kilmoter) {
        Kilmoter = kilmoter;
    }

    public void setTotalCharges(String totalCharges) {
        TotalCharges = totalCharges;
    }

    public String getGstAmount() {
        return GstAmount;
    }

    public void setGstAmount(String gstAmount) {
        GstAmount = gstAmount;
    }
}

