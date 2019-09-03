package com.sharpflux.supergodeliveryapp;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;


public class StepperIndicatorActivity extends AppCompatActivity
        implements StepOneFragment.OnStepOneListener,
        StepTwoFragment.OnStepTwoListener, StepThreeFragment.OnStepThreeListener
{

private SectionsPagerAdapter mSectionsPagerAdapter;


public NonSwipeableViewPager mViewPager;

private StepperIndicator stepperIndicator;
    Bundle bundle ;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_indicator);

   // setToolbarBackVisibility(true);
   //setTitle("Become Partner");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        stepperIndicator = findViewById(R.id.stepperIndicator);


        stepperIndicator.showLabels(false);
        stepperIndicator.setViewPager(mViewPager);
        // or keep last page as "end page"
        stepperIndicator.setViewPager(mViewPager, mViewPager.getAdapter().getCount() - 1); //

        /*// or manual change
        indicator.setStepCount(3);
        indicator.setCurrentStep(2);*/

         bundle = getIntent().getExtras();

        if(bundle.getString("Address")!= null)
        {
            String data= bundle.getString("Address");


            //bundle = new Bundle();
            bundle.putString("Address",data);
// set Fragmentclass Arguments
            StepOneFragment fragobj = new  StepOneFragment();
            fragobj.setArguments(bundle);

          /*

            StepThreeFragment fragThird = new  StepThreeFragment();
            fragThird.setArguments(bundle);*/


            //StepOneFragment fragment = StepOneFragment.newInstance(data, data);
           // getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();



        }

}

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*@Override
    public void sendData(String message) {
        //String tag = "android:switcher:" + R.id.viewPager + ":" + 1;
        // f = (FragmentTwo) getSupportFragmentManager().findFragmentByTag(tag);
       // f.displayReceivedData(message);
    }
*/
    public class SectionsPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return StepOneFragment.newInstance("", "");
            case 1:
                return StepTwoFragment.newInstance("", "","","","","");
            case 2:
                return StepThreeFragment.newInstance("", "","","","","");
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "First Level";
            case 1:
                return "Second Level";
            case 2:
                return "Finish";
        }
        return null;
    }
}


    @Override
    public void onNextPressed(Fragment fragment) {
        if (fragment instanceof StepOneFragment) {
            mViewPager.setCurrentItem(1, true);
        } else if (fragment instanceof StepTwoFragment) {
            mViewPager.setCurrentItem(2, true);
        } else if (fragment instanceof StepThreeFragment) {

            Toast.makeText(this, "Thanks For your order", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onBackPressed(Fragment fragment) {
        if (fragment instanceof StepTwoFragment) {
            mViewPager.setCurrentItem(0, true);
        } else if (fragment instanceof StepThreeFragment) {
            mViewPager.setCurrentItem(1, true);
        }
    }

    public void saveData(int id, Bundle data) {
        // based on the id you'll know which fragment is trying to save data(see below)
        // the Bundle will hold the data
    }
    public Bundle getSavedData() {
        // here you'll save the data previously retrieved from the fragments and
        // return it in a Bundle

        return bundle;
    }
}
