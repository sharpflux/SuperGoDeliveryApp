package com.sharpflux.supergodeliveryapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepTwoFragment extends Fragment implements View.OnClickListener, StepOneFragment.SendMessage {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_BUNDLE = "bundle";
    public static String DATEFORMATTED = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mBundle;
    private OnStepTwoListener mListener;
    AlertDialog.Builder builder;


    public StepTwoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param PickupAddress   Parameter 1.
     * @param DeliveryAddress Parameter 2.
     * @return A new instance of fragment BePartnerStepOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepTwoFragment newInstance(String PickupAddress, String DeliveryAddress, String FromLat, String FromLong, String Vehicle, String Product) {
        StepTwoFragment fragment = new StepTwoFragment();
        Bundle args = new Bundle();
        args.putString("PickupAddress", PickupAddress);
        args.putString("DeliveryAddress", DeliveryAddress);
        args.putString("FromLat", FromLat);
        args.putString("FromLong", FromLong);
        args.putString("Vehicle", Vehicle);
        args.putString("Product", Product);
        //args.putString(ARG_BUNDLE, Address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("PickupAddress");
            mParam2 = getArguments().getString("DeliveryAddress");
            mBundle = getArguments().getString("FromLat");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toast.makeText(getContext(), "Hello From Two" , Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_step_two, container, false);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeListener;

    private Button backBT;
    private Button prevBT;
    private TextView timeTextview;
    private TextView mDisplayDate;
    private EditText cpname, cnum, anum;
    private CardView cvdate, cvtime;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private String format = "";

    public static String PickupAddress;
    public static String DeliveryAddress;
    public static String FromLat;
    public static String FromLong;
    public static String ToLat;
    public static String ToLong;
    public static String Vehicle;
    public static String Product;
    public static String ImageUrl;
    Date date1;
    DatabaseHelper myDatabase;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        myDatabase = new DatabaseHelper(getContext());

        super.onViewCreated(view, savedInstanceState);

        builder = new AlertDialog.Builder(getContext());
        if (myDatabase.GetLastId() != "" && myDatabase.GetLastId() != "0") {
            Cursor res = myDatabase.DeliveryGETById(myDatabase.GetLastId());
            if (res.getCount() == 0) {

                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("PICKUPADDRESS  :" + res.getString(0) + "\n");
                buffer.append("FROMLAT  :" + res.getString(1) + "\n");
                buffer.append("FROMLONG  :" + res.getString(2) + "\n");
                buffer.append("DELIVERYADDRESS  :" + res.getString(3) + "\n\n");
            }
            //Uncomment the below code to Set the message and title from the strings.xml file
            builder.setMessage("Dialog").setTitle("Title");

            //Setting message manually and performing action on button click
            builder.setMessage(buffer.toString())
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Toast.makeText(getContext(), "you choose yes action for alertbox",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            Toast.makeText(getContext(), "you choose no action for alertbox",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("AlertDialogExample");
            alert.hide();
        }


        backBT = view.findViewById(R.id.buttonbackTwo);
        prevBT = view.findViewById(R.id.buttonPreview);
        timeTextview = view.findViewById(R.id.tvTime);
        mDisplayDate = view.findViewById(R.id.tvDate);
        cpname = view.findViewById(R.id.edittextcpname);
        cnum = view.findViewById(R.id.edittextcnum);
        anum = view.findViewById(R.id.edittextanum);

        cvdate = view.findViewById(R.id.cardviewdate);
        cvtime = view.findViewById(R.id.cardviewtime);


        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("custom-event-name"));

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mDisplayDate.setText(extras.getString("Vehicle"));
        }

        cvtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // showTime(mHour,mMinute);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeListener, mHour, mMinute, false);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));

                timePickerDialog.show();
            }
        });


        mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int min) {

                // hourOfDay = hourOfDay + 1;
                // hourOfDay = hourOfDay + 1;
                //  Log.d(TAG, "onDateSet: hh:mm: " + hourOfDay + ":" + minute);

                //String time = hourOfDay + ":" + minute;

                if (hour == 00) {
                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                timeTextview.setText(new StringBuilder().append(hour).append(" : ").append(min)
                        .append(" ").append(format));
                // mDisplayTime.setText(time);

            }
        };


        cvdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                cal.add(Calendar.MONTH,1);
                long afterTwoMonthsinMilli=cal.getTimeInMillis();


                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.getDatePicker().setMaxDate(afterTwoMonthsinMilli);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(getTag(), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + month + "/" + year;
                DATEFORMATTED = month + "/" + day + "/" + year;

                //String sDate1="31/12/1998";
                try {
                    date1 = new SimpleDateFormat("MM/dd/yyyy").parse(DATEFORMATTED);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mDisplayDate.setText(date);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        backBT.setOnClickListener(this);
        prevBT.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        backBT.setOnClickListener(null);
        prevBT.setOnClickListener(null);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("PickupAddress");
            PickupAddress = message;
            DeliveryAddress = intent.getStringExtra("DeliveryAddress");
            FromLat = intent.getStringExtra("FromLat");
            FromLong = intent.getStringExtra("FromLong");
            Vehicle = intent.getStringExtra("Vehicle");
            Product = intent.getStringExtra("Product");
            ToLat = intent.getStringExtra("ToLat");
            ToLong = intent.getStringExtra("ToLong");
            ImageUrl = intent.getStringExtra("ImageUrl");
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonbackTwo:
                if (mListener != null)
                    mListener.onBackPressed(this);
                break;

            case R.id.buttonPreview:
                if (mListener != null) {


                    //first getting the values
                    final String TimeS = timeTextview.getText().toString();
                    final String DateS = mDisplayDate.getText().toString();
                    final String CustomerName = cpname.getText().toString();
                    final String NumberCustomer = cnum.getText().toString();
                    final String AlterName = anum.getText().toString();


                    if (TextUtils.isEmpty(DateS)) {
                        mDisplayDate.setError("Please enter Date");
                        mDisplayDate.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(TimeS)) {
                        timeTextview.setError("Please enter Time");
                        timeTextview.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(CustomerName)) {
                        cpname.setError("Please enter Contact Person Name");
                        cpname.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(NumberCustomer)) {
                        cnum.setError("Please enter Contact Person Number");
                        cnum.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(AlterName)) {
                        anum.setError("Please enter Altername number");
                        anum.requestFocus();
                        return;
                    }


                    Intent intent = new Intent("fragment-three");
                    intent.putExtra("PickupAddress", PickupAddress.toString());
                    intent.putExtra("DeliveryAddress", DeliveryAddress.toString());
                    intent.putExtra("FromLat", FromLat.toString());
                    intent.putExtra("FromLong", FromLong.toString());
                    intent.putExtra("ToLat", ToLat.toString());
                    intent.putExtra("ToLong", ToLong.toString());
                    intent.putExtra("Vehicle", Vehicle.toString());
                    intent.putExtra("Product", Product.toString());
                    intent.putExtra("PickupDate", DATEFORMATTED.toString());
                    intent.putExtra("PickupTime", timeTextview.getText());
                    intent.putExtra("ContactPerson", cpname.getText().toString());
                    intent.putExtra("Mobile", cnum.getText().toString());
                    intent.putExtra("AlternateMobile", anum.getText().toString());
                    intent.putExtra("ImageUrl", ImageUrl);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

                    if (myDatabase.GetLastId() == "0") {
                        myDatabase.InsertDelivery(PickupAddress.toString().trim(),
                                FromLat, FromLong, DeliveryAddress.toString().trim(), ToLat, ToLong, Vehicle,
                                Product.toString().trim(), ImageUrl, DATEFORMATTED.toString(), timeTextview.getText().toString(), cpname.getText().toString(), anum.getText().toString());
                    } else {

                      myDatabase.UpdateDelivery(myDatabase.GetLastId(), PickupAddress.toString().trim(),
                                FromLat, FromLong, DeliveryAddress.toString().trim(), ToLat, ToLong, Vehicle,
                                Product.toString().trim(), ImageUrl, DATEFORMATTED.toString(), timeTextview.getText().toString(), cpname.getText().toString(), anum.getText().toString());
                    }


                    mListener.onNextPressed(this);
                    break;
                }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepTwoListener) {
            mListener = (OnStepTwoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        backBT = null;
        prevBT = null;
    }

    @Override
    public void PickupAddress(String PickupAddress) {
        Toast.makeText(getContext(), "Hello From Interface :::::" + PickupAddress, Toast.LENGTH_SHORT).show();
        //mDisplayDate.setText(PickupAddress);
    }


    public interface OnStepTwoListener {
        void onBackPressed(Fragment fragment);

        void onNextPressed(Fragment fragment);

    }

}
