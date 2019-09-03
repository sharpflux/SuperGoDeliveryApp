package com.sharpflux.supergodeliveryapp;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment {

    Button send,recieve,sell_button2;
    TextView txtUserGreet;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(NavigationDrawerConstants.TAG_HOME);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        send=view.findViewById(R.id.send_button);
        recieve=view.findViewById(R.id.sell_button);
        sell_button2=view.findViewById(R.id.sell_button);

        txtUserGreet=view.findViewById(R.id.txtUserGreet);


        //getting the current user
        User user = SharedPrefManager.getInstance(getContext()).getUser();
        txtUserGreet.setText("Hi "+user.getUsername());
       //customerName.setText("Hey "+user.getUsername()+"!");



       send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent sendIntent = new Intent(getContext(),MapsActivity.class);
                startActivity(sendIntent);

                /*   TrackFragment nextFrag= new TrackFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homeholder, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();*/
            }
        });



        sell_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent recieveIntent = new Intent(getContext(),MerchantListActivity.class);
                startActivity(recieveIntent);

            }
        });

    }

   /* private void swapFragment(){
        MerchantListActivity newGamefragment = new MerchantListActivity();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeholder, newGamefragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }*/
}
