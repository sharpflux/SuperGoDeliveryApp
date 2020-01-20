package com.sharpflux.supergodeliveryapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    //SendMessage SM;
    EditText txtflatNoHouse;
    EditText txtLandMark;
    Button confirmLocationButton;

    public static String PickupAddress;
    public static String DeliveryAddress;
    public static String FromLat;
    public static String FromLong;
    public static String Vehicle;
    public static String Product;
    public static String FlatNo;
    public static String LandMark2;
    DatabaseHelper myDatabase;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)

    {
        View v = inflater.inflate(R.layout.bottom_sheet_2, container, false);

         confirmLocationButton = (Button)v.findViewById(R.id.btnConfirmAddress);
         txtflatNoHouse = (EditText) v.findViewById(R.id.txtflatNoHouse);
         txtLandMark = (EditText) v.findViewById(R.id.txtLandMark);
          builder = new AlertDialog.Builder(getContext());
         LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("maps-get-delivery"));

            myDatabase = new DatabaseHelper(getContext());

          confirmLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first getting the values
                final String flatNo =  txtflatNoHouse.getText().toString();
                final String LandMark = txtLandMark.getText().toString();

                //validating inputs
                if (TextUtils.isEmpty(flatNo)) {
                    txtflatNoHouse.setError("Please enter Flat / House");
                    txtflatNoHouse.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(LandMark)) {
                    txtLandMark.setError("Please enter Land Mark");
                    txtLandMark.requestFocus();
                    return;
                }


              if (getArguments() != null) {

                      if (getArguments().getString("ActivityType").toString().equals("0")) {
                          Intent i = new Intent(getContext(), StepperIndicatorActivity.class);
                          i.putExtra("Address", getArguments().getString("Address").toString());
                          i.putExtra("FlatNo", txtflatNoHouse.getText().toString());
                          i.putExtra("LandMark", txtLandMark.getText().toString());
                          i.putExtra("FromLat", getArguments().getString("Lat"));
                          i.putExtra("FromLong", getArguments().getString("Long"));
                          i.putExtra("ActivityType", getArguments().getString("ActivityType").toString());

                          i.putExtra("DeliveryAddress", DeliveryAddress);
                          i.putExtra("DeliveryFlatNo", getArguments().getString("DeliveryFlatNo"));
                          i.putExtra("DeliveryLandMark", getArguments().getString("DeliveryLandMark"));
                          i.putExtra("ToLat", getArguments().getString("ToLat"));
                          i.putExtra("ToLong", getArguments().getString("ToLong"));


                          String PickUpAddress = txtflatNoHouse.getText().toString() + ", " + txtLandMark.getText().toString() + ", " + getArguments().getString("Address").toString();
                          if (myDatabase.GetLastId() != "" && myDatabase.GetLastId() != "0")
                              myDatabase.UpdatePickupAddress(myDatabase.GetLastId(), PickUpAddress, getArguments().getString("Lat"), getArguments().getString("Long"));


                          startActivity(i);
                      } else {
                          Intent i = new Intent(getContext(), StepperIndicatorActivity.class);
                          i.putExtra("PickupAddress", PickupAddress);
                          i.putExtra("PickupFlatNo", txtflatNoHouse.getText().toString());
                          i.putExtra("PickupLandMark", txtLandMark.getText().toString());
                          i.putExtra("FromLat", FromLat);
                          i.putExtra("FromLong", FromLong);
                          i.putExtra("Vehicle", Vehicle);
                          i.putExtra("Product", Product);
                          i.putExtra("ActivityType", getArguments().getString("ActivityType").toString());
                          i.putExtra("DeliveryAddress", getArguments().getString("Address").toString());
                          i.putExtra("DeliveryFlatNo", txtflatNoHouse.getText().toString());
                          i.putExtra("DeliveryLandMark", txtLandMark.getText().toString());
                          i.putExtra("ToLat", getArguments().getString("Lat"));
                          i.putExtra("ToLong", getArguments().getString("Long"));
                          i.putExtra("FlatNo", FlatNo);
                          i.putExtra("LandMark", LandMark2);


                          String DropAddress = txtflatNoHouse.getText().toString() + ", " + txtLandMark.getText().toString() + ", " + getArguments().getString("Address").toString();
                          if (myDatabase.GetLastId() != "" && myDatabase.GetLastId() != "0")
                              myDatabase.UpdateDeliveryAddress(myDatabase.GetLastId(), DropAddress, getArguments().getString("Lat"), getArguments().getString("Long"));


                          startActivity(i);
                      }
                  } else {

                  }


            }
        });
        TextView activitytype = (TextView) v.findViewById(R.id.activitytype);
        if (getArguments() != null) {


            EditText txtCurrentLocation = (EditText)v.findViewById(R.id.txtCurrentLocation);
            txtCurrentLocation.setText(getArguments().getString("Address").toString());
            if(getArguments().getString("ActivityType").toString().equals("0")){

                activitytype.setText("Pickup Address");
            }
            else {
                activitytype.setText("Delivery Address");
            }
        }
        else {
            activitytype.setText("Delivery Address");
        }

        Button btncancel = (Button)v.findViewById(R.id. btnCancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("PickupAddress");
            PickupAddress=message;
            DeliveryAddress=intent.getStringExtra("DeliveryAddress");
            FromLat=intent.getStringExtra("FromLat");
            FromLong=intent.getStringExtra("FromLong");
            Vehicle=intent.getStringExtra("Vehicle");
            Product=intent.getStringExtra("Product");
            FlatNo=intent.getStringExtra("FlatNo");
            LandMark2=intent.getStringExtra("LandMark");


        }
    };
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
               // dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        //super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_2, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String ReceiverName=data.getStringExtra("ReceiverName");
                String ReceiverMobile=data.getStringExtra("ReceiverMobile");

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }



}