package com.sharpflux.supergodeliveryapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class ActivityTrack extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentLocationMarker;
    private Location currentLocation;
    private boolean firstTimeFlag = true;
    LatLng pickLoc, dropLoc;
    public LatLng LatLong;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    SupportMapFragment supportMapFragment;
    Button btnAddMarker;
    Timer timer;
    String DeliveryId;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    public String LATLONGDB;
//    private final View.OnClickListener clickListener = view -> {
//        if (view.getId() == R.id.currentLocationImageButton && googleMap != null && currentLocation != null)
//            animateCamera(currentLocation);
//    };


    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
              /*if (locationResult.getLastLocation() == null)
                    return;
                 currentLocation = locationResult.getLastLocation();
                if (firstTimeFlag && googleMap != null) {
                    animateCamera(LatLng);
                    firstTimeFlag = false;
                }

                showMarker(currentLocation);*/

            Location location = new Location("Test");
            if (LATLONGDB != "" && LATLONGDB != null) {
                if (firstTimeFlag && googleMap != null) {

                    String[] latLng = LATLONGDB.split(",");
                    LatLong = new LatLng(Double.parseDouble(latLng[0].toString()), Double.parseDouble(latLng[1].toString()));


                    location.setLatitude(LatLong.latitude);
                    location.setLongitude(LatLong.longitude);
                    animateCamera(location);
                }


            }
            showMarker(location);

        }
    };
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                drawMarker(location);

            } else {
                Log.e("Location is null", "");
            }
        }

    };
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_order);
        timer = new Timer();
        LatLong = new LatLng(Double.parseDouble("18.5913"), Double.parseDouble("73.7389"));
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        DeliveryId="0";
        HubConnection hubConnection = HubConnectionBuilder.create("http://13.234.164.233:9922/chat").build();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            DeliveryId= extras.getString("DeliveryId");
        }
        btnAddMarker = findViewById(R.id.btnAddMarker);

        btnAddMarker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (LATLONGDB != "" && LATLONGDB != null) {
                    String[] latLng = LATLONGDB.split(",");
                    LatLong = new LatLng(Double.parseDouble(latLng[0].toString()), Double.parseDouble(latLng[1].toString()));
                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(ActivityTrack.this::onMapReady);
                }

            }
        });

        timer.schedule(new TimerTask() {
                           public void run() {
                               GetLocation();
                               if (LATLONGDB != "" && LATLONGDB != null) {
                                   String[] latLng = LATLONGDB.split(",");
                                   LatLong = new LatLng(Double.parseDouble(latLng[0].toString()), Double.parseDouble(latLng[1].toString()));



                                  // configureCameraIdle();
                               }

                           }
                       }, 4000, 15000
        );

        hubConnection.on("broadcastMessage", (OrderId, latLngPair) -> {
            Log.d("broadcastMessage", OrderId + "User " + latLngPair);
            String[] latLng = latLngPair.split(",");
            LatLong = new LatLng(Double.parseDouble(latLng[0].toString()), Double.parseDouble(latLng[1].toString()));
            Log.d("LAT LONG", LatLong.toString());
            btnAddMarker.callOnClick();

        }, String.class, String.class);

        hubConnection.on("UserConnected", (ConnectionId) -> {
            Log.d("UserConnected", "HH");
            hubConnection.send("JoinGroup", "777", ConnectionId.toString());

        }, String.class);
        hubConnection.on("UserDisconnected", (ConnectionId) -> {
            Log.d("UserDisconnected", "DISCONNECTED");

        }, String.class);


        new HubConnectionTask().execute(hubConnection);
        //findViewById(R.id.currentLocationImageButton).setOnClickListener(clickListener);


    }

    private void configureCameraIdle() {

        LatLng latLng = LatLong;
        Location driverCurrentLoc = new Location("");
        driverCurrentLoc.setLatitude(LatLong.latitude);
        driverCurrentLoc.setLongitude(LatLong.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLong, 12));
        googleMap.setOnMapLoadedCallback(this);

    }

    private void GetLocation() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GETLOCATION + "?DeliveryId=49",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressBar.setVisibility(View.GONE);
                        try {


                            JSONArray obj = new JSONArray(response);
                            //if no error in response
                            //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < obj.length(); i++) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject(i);

                                LATLONGDB = userJson.getString("LatLong");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SellerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //   params.put("ImageUrl", "");
//                params.put("CategoryName_EN", "");
                return params;
            }
        };

        VolleySingleton.getInstance(ActivityTrack.this).addToRequestQueue(stringRequest);


    }


    @Override
    public void onMapLoaded() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(LatLong).zoom(12f).tilt(15).build();

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setBuildingsEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        googleMap.addMarker(new MarkerOptions()
                .position(LatLong)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));

    }


    private void drawMarker(Location location) {
        if (googleMap != null) {
            googleMap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Current Position"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
        }

    }


    public LatLng convertStringToLatLng(String latLngPair) {
        String[] latLng = latLngPair.split(",");
        double latitude = Double.parseDouble(latLng[0].substring(1, latLng[0].length()));
        double longitude = Double.parseDouble(latLng[1].substring(0, latLng[1].length() - 1));
        return new LatLng(latitude, longitude);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(LatLong);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(markerOptions);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(LatLong)             // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(10)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(LatLong);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(LatLong)             // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(10)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
    public void onConnected(@Nullable Bundle bundle) {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = LatLong;
        Location driverCurrentLoc = new Location("");
        driverCurrentLoc.setLatitude(LatLong.latitude);
        driverCurrentLoc.setLongitude(LatLong.longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLong, 12));
        googleMap.setOnMapLoadedCallback(this);
    }


    class HubConnectionTask extends AsyncTask<HubConnection, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(HubConnection... hubConnections) {
            HubConnection hubConnection = hubConnections[0];
            hubConnection.start().blockingAwait();
            return null;
        }
    }

    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityTrack.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Permission denied by uses", Toast.LENGTH_SHORT).show();
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();
        }
    }

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    private void showMarker(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (currentLocationMarker == null)
            currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng));
        // else
        // MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (fusedLocationProviderClient != null)
//            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            startCurrentLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient = null;
        googleMap = null;
    }

    private void updateUI(Map<String, String> newLoc) {
        LatLng newLocation = new LatLng(Double.valueOf(newLoc.get("lat")), Double.valueOf(newLoc.get("lng")));
        if (currentLocationMarker != null) {
            animateCar(newLocation);
            boolean contains = googleMap.getProjection()
                    .getVisibleRegion()
                    .latLngBounds
                    .contains(newLocation);
            if (!contains) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
            }
        } else {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    newLocation, 15.5f));
            currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(newLocation).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
        }
    }

    private void animateCar(final LatLng destination) {
        final LatLng startPosition = currentLocationMarker.getPosition();
        final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);
        final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(5000); // duration 5 seconds
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float v = animation.getAnimatedFraction();
                    LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                    currentLocationMarker.setPosition(newPosition);
                } catch (Exception ex) {
                }
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();
    }

    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

}