package com.sharpflux.supergodeliveryapp;

public class URLs {

    public static final String URL_REGISTER = "http://api.supergo.in/api/CustomerApi/CustomerRegister";
    public static final String URL_LOGIN= "http://api.supergo.in/api/LogIn/CustomerLogin";
    public static final String URL_RATECALCULATOR= "http://api.supergo.in/api/RateCalculator/RateCalculator";

    public static final String URL_DETAILS="http://api.supergo.in/api/DeliveryApi/InsertDelivery";
    public static final String URL_FIREBASE="https://fcm.googleapis.com/fcm/send";
    public static final String URL_FIREBASE_SEND_NOTIFICATION="http://api.supergo.in/api/Notification/ExcutePushNotification";

    public  static  final String URL_DELIVERIES="http://api.supergo.in/api/GetAllDelivery/DeliveryRequestsall";

    public static final String URL_OTP="http://admin.supergo.in/Utilities/OTPGenerate";
    public static final String URL_GETLOCATION="http://admin.supergo.in/api/LocationTrackerGETCustomer";

    public static final String URL_RECYCLER="http://api.supergo.in/api/MerchantType/MerchantTypesGet";
    public static final String URL_AllMERCHANT="http://admin.supergo.in/api/MerchantsApi/Get_MerchantApi?MerchantTypeId=";
    public static final String URL_MERCHANTDESCRIPTION="http://admin.supergo.in/api/ItemMasterApi/Get_ItemApi?MerchantId=";
    public static final String URL_DESCIMAGES="http://admin.supergo.in/api/MerchantImageApi/Get_MerchantImagesUrlApi?MerchantId=";
    public static final String URL_ORDERDETAILS="http://admin.supergo.in/api/Customer/GetDeliveries?CustomerId=";
    public static final String URL_RESETPASS="http://admin.supergo.in/api/User/ChangePassword";


}
