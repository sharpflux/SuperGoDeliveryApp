package com.sharpflux.supergodeliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ChooseActionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView navBarName, navMobileNumber, tv_location, tv_current_loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_action);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);


        header = navigationView.getHeaderView(0);

        //Select Home by default
        navigationView.setCheckedItem(R.id.nav_home);
        Fragment fragment = new HomeFragment();
        displaySelectedFragment(fragment);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, CustomerLoginActivity.class));
        }


        //send=findViewById(R.id.textviewSend);
        //recieve=findViewById(R.id.textviewRecieve);
        //customerName=findViewById(R.id.textViewCustomerName);
        navBarName = header.findViewById(R.id.navheaderName);
        navMobileNumber = header.findViewById(R.id.navheaderMobile);


        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //customerName.setText("Hey "+user.getUsername()+"!");
        navBarName.setText("Hey " + user.getUsername() + "!");
        navMobileNumber.setText("+91" + user.getMobile());


        tv_location = findViewById(R.id.tv_location);

        tv_current_loc = findViewById(R.id.tv_current_loc);

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(ChooseActionActivity.this, MapsActivity.class);
                startActivity(sendIntent);
            }
        });

        tv_current_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(ChooseActionActivity.this, MapsActivity.class);
                startActivity(sendIntent);
            }
        });

       /* send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(ChooseActionActivity.this,MapsActivity.class);
                startActivity(sendIntent);
            }
        });

        recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent recieveIntent = new Intent(ChooseActionActivity.this,MapsActivity.class);
                startActivity(recieveIntent);

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout2) {


            finish();
            SharedPrefManager.getInstance(getApplicationContext()).logout();

            return true;
        } else if (id == R.id.nav_order2) {


            Fragment fragment = null;
            fragment = new OrderFragment();
            displaySelectedFragment(fragment);

            return true;
        } else if (id == R.id.nav_offers2) {
            Fragment fragment = null;
            fragment = new OffersFragment();
            displaySelectedFragment(fragment);

            return true;
        }/*else if(id == R.id.nav_settings2)
        {
            Intent pin = new Intent(ChooseActionActivity.this,ProfileSettingsActivity.class);
            startActivity(pin);

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Fragment fragment = null;
            fragment = new HomeFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.nav_order) {

            // Intent oin = new Intent(ChooseActionActivity.this,MyOrderListActivity.class);
            // startActivity(oin);

            Fragment fragment = null;
            fragment = new OrderFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.nav_offers) {
            Fragment fragment = null;
            fragment = new OffersFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.nav_contactus) {

            //Open URL on click of Visit Us
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NavigationDrawerConstants.SITE_URL));
            startActivity(urlIntent);

        } else if (id == R.id.nav_help) {

            //Open URL on click of Visit Us
            Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NavigationDrawerConstants.SITE_URL));
            startActivity(urlIntent);

        } else if (id == R.id.nav_share) {

            //Display Share Via dialogue
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType(NavigationDrawerConstants.SHARE_TEXT_TYPE);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, NavigationDrawerConstants.SHARE_TITLE);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, NavigationDrawerConstants.SHARE_MESSAGE);
            startActivity(Intent.createChooser(sharingIntent, NavigationDrawerConstants.SHARE_VIA));


        } else if (id == R.id.nav_rate) {
            appLink();

        } else if (id == R.id.nav_logout) {
            //when the user presses logout button
            //calling the logout method

            finish();
            SharedPrefManager.getInstance(getApplicationContext()).logout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void appLink() {

       /* String market_uri ="https://play.google.com/store/apps/details?id=com.supergo.customer";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(market_uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                .FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);*/

        // final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.supergo.customer")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.supergo.customer")));
        }
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}
