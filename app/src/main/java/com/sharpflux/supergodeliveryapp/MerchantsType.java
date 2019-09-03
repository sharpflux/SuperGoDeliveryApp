package com.sharpflux.supergodeliveryapp;

public class MerchantsType {

    public String MerchantId;
    public String ImageUrl;
    public String TypeName;

    public MerchantsType(String merchantId, String imageUrl, String typeName) {
        MerchantId = merchantId;
        ImageUrl = imageUrl;
        TypeName = typeName;
    }

    public String getMerchantId() {
        return MerchantId;
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
}
