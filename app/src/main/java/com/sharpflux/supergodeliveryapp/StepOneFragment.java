package com.sharpflux.supergodeliveryapp;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class StepOneFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static  String FROMLAT = "";
    private static  String FROMLONG = "";
    private static  String TOLAT = "";
    private static  String TOLONG = "";
    private static  String FlatNo = "";
    private static  String LandMark = "";
    private static  String FlatNoDrop = "";
    private static  String LandMarkDrop = "";
    private int buttonState = 1;
    private String mParam1;
    private String mParam2;
    private ActivityCommunicator activityCommunicator;
    private OnStepOneListener mListener;

    public  static final int RequestPermissionCode  = 1 ;
    Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    CheckBox chkBike;
    CheckBox chkCar;
    CheckBox chkVan;
    CheckBox chkTruck;
    CheckBox chkEbike;
    public String ImageUrl;
    SendMessage SM;

    DatabaseHelper myDatabase;

    public FragmentCommunicator fragmentCommunicator;

    public StepOneFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StepOneFragment newInstance(String param1, String param2) {
        StepOneFragment fragment = new StepOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //Log.e("heloooooooooooooo",mParam2);
        }

        // handle intent extras

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String strtext = getArguments().getString("Address");
        // Log.e("heloooooooooooooo",strtext);
        return inflater.inflate(R.layout.fragment_step_one, container, false);
    }

    TextView tvpickup;
    private Button nextBT;
    private Button backBT, car, bike, van, truck;
    private TextView editTextdeliveryaddress,textviewPickup,textforgot;
    private EditText edittextProduct;
    private TextView hideImageTv;
    ImageView product_imageView;
    Button btn_productimage;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextBT = view.findViewById(R.id.buttonNext);
        backBT = view.findViewById(R.id.buttonBack);
        myDatabase = new DatabaseHelper(getContext());

        editTextdeliveryaddress= view.findViewById(R.id.edittextDeliveryAddress);
        textviewPickup= view.findViewById(R.id.textviewPickup);
        textforgot= view.findViewById(R.id.textforgot);

        ImageUrl="";
        chkBike = view.findViewById(R.id.chkBike);
        chkCar = view.findViewById(R.id.chkCar);
        chkVan = view.findViewById(R.id.chkVan);
        chkTruck = view.findViewById(R.id.chkTruck);
        chkEbike=view.findViewById(R.id.chkEbike);
        tvpickup=view.findViewById(R.id.textviewPickup);
        edittextProduct=view.findViewById(R.id.edittextProduct);
        hideImageTv=view.findViewById(R.id.hideImageTv);


        product_imageView=view.findViewById(R.id.product_imageView);
        btn_productimage=view.findViewById(R.id.btn_productimage);

        chkEbike.setChecked(false);
        chkBike.setChecked(false);
        chkCar.setChecked(false);
        chkVan.setChecked(false);
        chkTruck.setChecked(false);

        chkEbike.setTextColor(Color.parseColor("#4B4B4B"));
        chkBike.setTextColor(Color.parseColor("#4B4B4B"));
        chkCar.setTextColor(Color.parseColor("#4B4B4B"));
        chkTruck.setTextColor(Color.parseColor("#4B4B4B"));
        chkVan.setTextColor(Color.parseColor("#4B4B4B"));

        if(myDatabase.GetLastId()!="" && myDatabase.GetLastId()!="0" ) {
            Cursor res = myDatabase.DeliveryGETById(myDatabase.GetLastId());
            if (res.getCount() == 0) {

            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                tvpickup.setText(res.getString(1));
                FROMLAT = res.getString(2);
                FROMLONG = res.getString(3);

                editTextdeliveryaddress.setText(res.getString(4));
                TOLAT = res.getString(5);
                TOLONG = res.getString(6);

                CheckBoxDynamic(res.getString(7));
                edittextProduct.setText(res.getString(8));


            }
        }else {
              Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {

            if(extras.getString("ActivityType").toString().equals("0")){

                tvpickup.setText(extras.getString("FlatNo")+","+extras.getString("LandMark")+"," +
                        ""+extras.getString("Address"));
                if(extras.getString("PickupFlatNo")!=null && extras.getString("DeliveryLandMark")!=null) {
                    editTextdeliveryaddress.setText(extras.getString("PickupFlatNo") + "," +
                            "" + extras.getString("DeliveryLandMark") + "," + extras.getString("DeliveryAddress"));
                }

                FROMLAT= extras.getString("FromLat")  ;
                FROMLONG=extras.getString("FromLong");
                FlatNo=extras.getString("FlatNo")  ;
                LandMark=extras.getString("LandMark")  ;

                TOLAT=extras.getString("ToLat")  ;
                TOLONG=extras.getString("ToLong");
            }


            else {

                if(extras.getString("PickupFlatNo")!=null && extras.getString("DeliveryLandMark")!=null) {
                    editTextdeliveryaddress.setText(extras.getString("PickupFlatNo") + "," +
                            "" + extras.getString("DeliveryLandMark") + "," + extras.getString("DeliveryAddress"));
                }

                tvpickup.setText(extras.getString("FlatNo")+"," +
                        ""+extras.getString("LandMark")+","+extras.getString("PickupAddress"));
                FROMLAT= extras.getString("FromLat")  ;
                FROMLONG=extras.getString("FromLong");
                TOLAT=extras.getString("ToLat")  ;
                TOLONG=extras.getString("ToLong");
                FlatNoDrop=extras.getString("PickupFlatNo");
                LandMarkDrop=extras.getString("DeliveryLandMark");


            }


        }
        }

        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MapsActivity.class);
                startActivity(i);
            }
        });



        nextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.PickupAddress(textviewPickup.getText().toString().trim());

                // Toast.makeText(getContext(), "Hello From Interface Clicl :::::"+textviewPickup.getText().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        textviewPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("maps-get-delivery");
                intent.putExtra("ActivityType", "0");
                intent.putExtra("PickupAddress", textviewPickup.getText().toString().trim());
                intent.putExtra("FromLat", FROMLAT);
                intent.putExtra("FromLong", FROMLONG);
                intent.putExtra("FlatNo", FlatNo);
                intent.putExtra("LandMark", LandMark);
                intent.putExtra("ToLat", TOLAT);
                intent.putExtra("ToLong", TOLONG);
                intent.putExtra("Vehicle", getSelectedCheckBox());
                intent.putExtra("Product", edittextProduct.toString());

                intent.putExtra("DeliveryAddress", editTextdeliveryaddress.getText().toString().trim());
                intent.putExtra("DeliveryFlatNo", FlatNoDrop);
                intent.putExtra("DeliveryLandMark", LandMarkDrop);
                intent.putExtra("ToLat",  TOLAT);
                intent.putExtra("ToLong",  TOLONG);

                Intent i = new Intent(getContext(), MapsActivity.class);
                i.putExtra("ActivityType", "0");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                startActivity(i);

            }
        });

        editTextdeliveryaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("maps-get-delivery");
                intent.putExtra("ActivityType", "1");
                intent.putExtra("PickupAddress", textviewPickup.getText().toString().trim());
                intent.putExtra("FromLat", FROMLAT);
                intent.putExtra("FromLong", FROMLONG);
                intent.putExtra("FlatNo", FlatNo);
                intent.putExtra("LandMark", LandMark);
                intent.putExtra("ToLat", TOLAT);
                intent.putExtra("ToLong", TOLONG);
                intent.putExtra("Vehicle", getSelectedCheckBox());
                intent.putExtra("Product", edittextProduct.toString());
                Intent i = new Intent(getContext(), MapsActivity.class);
                i.putExtra("ActivityType", "1");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                startActivity(i);

            }
        });



        chkEbike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    chkCar.setChecked(false);
                    chkTruck.setChecked(false);
                    chkVan.setChecked(false);
                    chkBike.setChecked(false);

                    chkEbike.setTextColor(Color.parseColor("#0A1C8B"));
                    chkBike.setTextColor(Color.parseColor("#4B4B4B"));
                    chkCar.setTextColor(Color.parseColor("#4B4B4B"));
                    chkTruck.setTextColor(Color.parseColor("#4B4B4B"));
                    chkVan.setTextColor(Color.parseColor("#4B4B4B"));

                }

            }
        });

        chkBike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    chkCar.setChecked(false);
                    chkTruck.setChecked(false);
                    chkVan.setChecked(false);
                    chkEbike.setChecked(false);
                    chkEbike.setTextColor(Color.parseColor("#4B4B4B"));

                    chkBike.setTextColor(Color.parseColor("#0A1C8B"));
                    chkCar.setTextColor(Color.parseColor("#4B4B4B"));
                    chkTruck.setTextColor(Color.parseColor("#4B4B4B"));
                    chkVan.setTextColor(Color.parseColor("#4B4B4B"));
                }

            }
        });


        chkCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    chkBike.setChecked(false);
                    chkTruck.setChecked(false);
                    chkVan.setChecked(false);

                    chkCar.setTextColor(Color.parseColor("#0A1C8B"));
                    chkBike.setTextColor(Color.parseColor("#4B4B4B"));
                    chkTruck.setTextColor(Color.parseColor("#4B4B4B"));
                    chkVan.setTextColor(Color.parseColor("#4B4B4B"));
                    chkEbike.setChecked(false);
                    chkEbike.setTextColor(Color.parseColor("#4B4B4B"));


                }

            }
        });

        chkVan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {

                    chkCar.setChecked(false);

                    chkBike.setChecked(false);

                    chkTruck.setChecked(false);

                    chkVan.setTextColor(Color.parseColor("#0A1C8B"));
                    chkCar.setTextColor(Color.parseColor("#4B4B4B"));
                    chkBike.setTextColor(Color.parseColor("#4B4B4B"));
                    chkTruck.setTextColor(Color.parseColor("#4B4B4B"));
                    chkEbike.setChecked(false);
                    chkEbike.setTextColor(Color.parseColor("#4B4B4B"));
                }

            }
        });

        chkTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    chkCar.setChecked(false);
                    chkVan.setChecked(false);
                    chkBike.setChecked(false);

                    chkTruck.setTextColor(Color.parseColor("#0A1C8B"));
                    chkCar.setTextColor(Color.parseColor("#4B4B4B"));
                    chkVan.setTextColor(Color.parseColor("#4B4B4B"));
                    chkBike.setTextColor(Color.parseColor("#4B4B4B"));
                    chkEbike.setChecked(false);
                    chkEbike.setTextColor(Color.parseColor("#4B4B4B"));
                }
            }
        });

        EnableRuntimePermission();
        btn_productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelecteImages();
                hideImageTv.setText("hello");

            }
        });

    }

    private void SelecteImages(){

        final CharSequence[] items={"Camera","Gallary","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(items[i].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }

               /* else if(items[i].equals("Gallary")){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
                }*/

                else if(items[i].equals("Cancel"))
                {
                    hideImageTv.clearComposingText();
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode,data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                ImageUrl= Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;

                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        product_imageView.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        ImageUrl= Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        product_imageView.setImageBitmap(thumbnail);



    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(), Manifest.permission.CAMERA))
        {

            Toast.makeText(getContext(),"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(),"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getContext(),"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }





    @Override
    public void onResume() {
        super.onResume();
        nextBT.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        nextBT.setOnClickListener(null);
    }

    String data;
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonNext:
                if (mListener != null) {

                    //first getting the values
                    final String Pickup =  textviewPickup.getText().toString();
                    final String delivery = editTextdeliveryaddress.getText().toString();
                    final String product = edittextProduct.getText().toString();
                    final String image = hideImageTv.getText().toString();


                    final String CheckBoxText=getSelectedCheckBox();
                    //validating inputs
                    if (TextUtils.isEmpty(Pickup)) {
                        textviewPickup.setError("Please enter Pickup Address");
                        textviewPickup.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(delivery)) {
                        editTextdeliveryaddress.setError("Please enter Delivery Address");
                        editTextdeliveryaddress.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(CheckBoxText)) {
                        textforgot.setError("Please enter Delivery Address");
                        textforgot.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(product)) {
                        edittextProduct.setError("Please Upload Product details");
                        edittextProduct.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(image)) {
                        btn_productimage.setError("Please Upload Product Image");
                        btn_productimage.requestFocus();
                        return;
                    }

                    if(ImageUrl.equals("")){

                    }

                    Intent intent = new Intent("custom-event-name");
                    intent.putExtra("PickupAddress", textviewPickup.getText().toString().trim());
                    intent.putExtra("DeliveryAddress", editTextdeliveryaddress.getText().toString().trim());
                    intent.putExtra("FromLat", FROMLAT);
                    intent.putExtra("FromLong", FROMLONG);
                    intent.putExtra("ToLat", TOLAT);
                    intent.putExtra("ToLong", TOLONG);
                    intent.putExtra("Vehicle", CheckBoxText);
                    intent.putExtra("Product", edittextProduct.getText().toString().trim());
                    intent.putExtra("ImageUrl", ImageUrl);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

                    mListener.onNextPressed(this);
                    break;
                }
        }

    }


    public  void CheckBoxDynamic(String Vehicle){


        chkBike.setChecked(false);
        chkCar.setChecked(false);
        chkVan.setChecked(false);
        chkTruck.setChecked(false);
        chkEbike.setChecked(false);
        if(Vehicle.equals("Bike")) {

            chkCar.setChecked(false);
            chkTruck.setChecked(false);
            chkVan.setChecked(false);
            chkEbike.setChecked(false);
            chkEbike.setTextColor(Color.parseColor("#4B4B4B"));

            chkBike.setTextColor(Color.parseColor("#0A1C8B"));
            chkCar.setTextColor(Color.parseColor("#4B4B4B"));
            chkTruck.setTextColor(Color.parseColor("#4B4B4B"));
            chkVan.setTextColor(Color.parseColor("#4B4B4B"));
            chkBike.setChecked(true);
        }
        else  if(Vehicle.equals("Car")) {

            chkBike.setChecked(false);
            chkTruck.setChecked(false);
            chkVan.setChecked(false);

            chkCar.setTextColor(Color.parseColor("#0A1C8B"));
            chkBike.setTextColor(Color.parseColor("#4B4B4B"));
            chkTruck.setTextColor(Color.parseColor("#4B4B4B"));
            chkVan.setTextColor(Color.parseColor("#4B4B4B"));
            chkEbike.setChecked(false);
            chkEbike.setTextColor(Color.parseColor("#4B4B4B"));
            chkCar.setChecked(true);
        }
        else  if(Vehicle.equals("Van")) {
            chkVan.setChecked(true);
        }
        else  if(Vehicle.equals("Truck")) {
            chkTruck.setChecked(true);
        }
        else  if(Vehicle.equals("E-Bike")) {
            chkEbike.setChecked(true);
        }

    }

    public  String getSelectedCheckBox(){
        boolean chkEBikeChecked = (chkEbike).isChecked();
        boolean chkBikeChecked = (chkBike).isChecked();
        boolean chkCarChecked = (chkCar).isChecked();
        boolean chkVanChecked = (chkVan).isChecked();
        boolean chkTruckChecked = (chkTruck).isChecked();
        // boolean chkEbike = (ch).isChecked();

        chkBike.setChecked(false);
        chkCar.setChecked(false);
        chkVan.setChecked(false);
        chkTruck.setChecked(false);
        //  chkEbike.setChecked(false);

        String checkedVehicle = "";

        if (chkBikeChecked == true) {
            checkedVehicle = "Bike";
        }

        if (chkEBikeChecked == true) {
            checkedVehicle = "E-Bike";
        }
        if (chkCarChecked == true) {
            checkedVehicle = "Car";
        }
        if (chkVanChecked == true) {
            checkedVehicle = "Van";
        }
        if (chkTruckChecked == true) {
            checkedVehicle = "Truck";
        }
        return  checkedVehicle;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnStepOneListener) {
            mListener = (OnStepOneListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        nextBT = null;
    }

    public interface OnStepOneListener {
        //void onFragmentInteraction(Uri uri);
        void onNextPressed(Fragment fragment);
    }
    public  interface SendMessage {
        void PickupAddress(String PickupAddress);

    }
    public interface FragmentCommunicator{
        void passDataToFragment(String someValue);
    }

    public interface ActivityCommunicator{
        void passDataToActivity(String someValue);
    }

}
