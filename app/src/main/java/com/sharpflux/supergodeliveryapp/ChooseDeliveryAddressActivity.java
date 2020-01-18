package com.sharpflux.supergodeliveryapp;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ChooseDeliveryAddressActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private Marker marker;
    private LocationRequest locationRequest;
    private static final int MIN_DISPLACEMENT = 10;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static boolean LOCATION_CHANGE = false;
    Button btnProcess;
    ImageView imageView;
    String Activityname;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    AlertDialog.Builder builder;
    String FromLat,FromLong,MerchantId,MerchantAddress,TotalCharges,GstAmount;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        imageView=(ImageView)findViewById(R.id.imageView);
        btnProcess=(Button) findViewById(R.id.btnProcess);


        builder = new AlertDialog.Builder(this);

        btnProcess.setText("L O C A T I N G...");

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Intent iin = getIntent();
        b = iin.getExtras();
        if(b!=null) {

            FromLat=b.getString("FromLat");
            FromLong=b.getString("FromLong");
            MerchantId=b.getString("MerchantId");
            MerchantAddress=b.getString("MerchantAddress");
            TotalCharges = b.getString("TotalCharges");
            GstAmount=b.getString("GstAmount");
        }



        //Place Picker Code

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBDl1LtAS21s-0JkYMEC0JgMLKf5jyJqi8");
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("EERR", "Place: " + place.getName() + ", " + place.getId());
                Log.e("THIS IS ME", "THiS IS MEEEEEE");
                mMap.clear();


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng())             // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(10)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("ERD", "An error occurred: " + status);
                Log.e("THIS IS ME 2", "THiS IS MEEEEEE 2");
            }
        });

        autocompleteFragment.setCountry("IN");

        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(MIN_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        View showModalBottomSheet = findViewById(R.id.btnProcess);
        showModalBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                LatLng center=mMap.getCameraPosition().target;
                //Initializing a bottom sheet
                ChooseDeliveryBottomSheetDialog bottomSheetDialogFragment = new ChooseDeliveryBottomSheetDialog();
                bottomSheetDialogFragment.setCancelable(false);
                if(center.latitude!=0.0 &&center.longitude!=0.0){
                    Bundle args = new Bundle();
                    Log.e("LATI",String.valueOf(center.latitude));
                    args.putString("Address",   getAddress(getApplicationContext(),center.latitude,center.longitude).toString());
                    args.putString("Lat",  Double.toString(center.latitude )  );
                    args.putString("Long", Double.toString(center.longitude) );
                    args.putString("MerchantTypeId", b.getString("MerchantTypeId"));
                    args.putString("MerchantId", b.getString("MerchantId"));
                    args.putString("MerchantName",b.getString("MerchantName"));
                    args.putString("mobilenum", "");
                    args.putString("FromLat", b.getString("FromLat"));
                    args.putString("FromLong", b.getString("FromLong"));
                    args.putString("MerchantAddress", b.getString("MerchantAddress"));
                    args.putString("TotalCharges", b.getString("TotalCharges"));
                    args.putString("GstAmount", b.getString("GstAmount"));
                    args.putString("ImageUrl", b.getString("ImageUrl"));
                    args.putString("Speciality", b.getString("Speciality"));
                    args.putString("ActivityType",   "Merchant" );
                    bottomSheetDialogFragment.setArguments(args);
                    //show it
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
                else
                {
                    builder.setMessage("Please select address first")
                            .setCancelable(false)

                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Invalid Address");
                    alert.show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (b != null) {
            Intent intent = new Intent(getApplicationContext(), CheckOutCart.class);
            intent.putExtra("MerchantTypeId",b.getString("MerchantTypeId"));
            intent.putExtra("MerchantId", b.getString("MerchantId"));
            intent.putExtra("MerchantName",b.getString("MerchantName"));
            intent.putExtra("mobilenum", "");
            intent.putExtra("FromLat", b.getString("FromLat"));
            intent.putExtra("FromLong", b.getString("FromLong"));
            intent.putExtra("MerchantAddress", b.getString("MerchantAddress"));
            intent.putExtra("TotalCharges", b.getString("TotalCharges"));
            intent.putExtra("GstAmount", b.getString("GstAmount"));
            intent.putExtra("ImageUrl", b.getString("ImageUrl"));
            intent.putExtra("Speciality", b.getString("Speciality"));
            startActivity(intent);
        }
        finish();
    }
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Config.PUSH_NOTIFICATION)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e("", "Firebase reg id: " + regId);
    }


    int de =0;
    @Override
    public void onLocationChanged(Location location) {
        Log.i("called", "onLocationChanged");

        btnProcess.setText("Choose Delivery Address");
        btnProcess.setEnabled(true);
        btnProcess.setBackgroundResource(R.drawable.btn_plain );

        mMap.clear();
        LatLng center=mMap.getCameraPosition().target;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(LOCATION_CHANGE==false){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 18));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(10)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }



        MarkerOptions option = new MarkerOptions();

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center=mMap.getCameraPosition().target;

                if(marker!=null){
                    marker.remove();
                    marker=  mMap.addMarker(new MarkerOptions().position(center).title("New Position"));


                    Log.e("Lat Long" ,center.toString() );
                }

            }
        });


        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                LOCATION_CHANGE=true;
                LatLng center=mMap.getCameraPosition().target;

                getAddress(getApplicationContext(),center.latitude,center.longitude);
            }
        });

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
// Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
// Show an explanation to the user *asynchronously* -LocationSettingsRequest- don't block
// this thread waiting for the user's response! After the user
// sees the explanation, try again to request the permission.
//Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
// No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// permission was granted. Do the
// contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        btnProcess.setText("Choose Delivery Address");
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.d("STARR", "Location update resumed .....................");

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    // Get Address from latitude and longitude //
    public String getAddress(Context ctx, double lat,double lng){
        String fullAdd=null;
        try{
            Geocoder geocoder= new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat,lng,1);
            if(addresses.size()>0){
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);

                // if you want only city or pin code use following code //
                   /* String Location = address.getLocality();
                    String zip = address.getPostalCode();
                    String Country = address.getCountryName(); */
            }


        }catch(IOException ex){
            ex.printStackTrace();
        }

        //  Toast.makeText(MapsActivity.this, "Address "+fullAdd, Toast.LENGTH_SHORT).show();

       /* EditText txtCurrentLocation=(EditText)findViewById(R.id.txtCurrentLocation);
        txtCurrentLocation.setText(fullAdd.toString());*/
        btnProcess.setText("Choose Delivery Address");
        btnProcess.setEnabled(true);
        btnProcess.setBackgroundResource(R.drawable.btn_plain);
        return fullAdd;
    }



}