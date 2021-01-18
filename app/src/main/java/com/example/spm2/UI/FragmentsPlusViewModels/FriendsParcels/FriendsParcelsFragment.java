package com.example.spm2.UI.FragmentsPlusViewModels.FriendsParcels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.spm2.Entities.Parcel;
import com.example.spm2.R;
import com.example.spm2.UI.MainActivity;
import com.example.spm2.UI.NavigationDrawer;
import com.example.spm2.Utils.ParcelStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendsParcelsFragment extends Fragment {

    List <Parcel> itemList = new ArrayList<Parcel>();
    private FriendsParcelsViewModel friendsParcelsViewModel;

    static public String recipientPhoneNumber;
    static public String recipientEmailAddress;
    Context context= getContext();
    public static EditText deliveryManAddress;

    private TextView TName;
    private TextView info;
    private Button buttonClose;
   // private Button buttonClose2;
   //private Button buttonClose3;
   // private Button buttonConfirm;
    private Button buttonCall;
    private CheckBox buttonCheckboxOffer;
    private CheckBox buttonCheckboxPickUP;
    private Button buttonSms;
    private Button buttonMail;
    private SearchView searchview;
    private ScrollView openDetails;
   // private LinearLayout deliverdDetails;
    //private LinearLayout RecievedOrOnItsWayLinearLayout;
    ListView listView;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String userEmail=user.getEmail();
    static public String postManAddress="";
    Parcel p;
    ItemArrayAdapter itemArrayAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_friends_parcels, container, false);
        listView = root.findViewById(R.id.listView);
        //getting delivery man address

        itemArrayAdapter = new ItemArrayAdapter(getActivity(), itemList,root);
        listView.setAdapter(itemArrayAdapter);
        setUpListItemClickListener();

        friendsParcelsViewModel = ViewModelProviders.of(getActivity()).get(FriendsParcelsViewModel.class);
        friendsParcelsViewModel.getAllParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {

                @Override
                public void onChanged(List<Parcel> parcelList) {
                    boolean listChanged = false;
                    for (Parcel parcel: parcelList)
                    {
                        itemList.add(parcel);
                        listChanged = true;
                    }
                    if(listChanged) {
                        //((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                        itemArrayAdapter.notifyDataSetChanged();
                    }
            }
        });




        EditText spmAddress=root.findViewById(R.id.postManAddress);
        postManAddress=spmAddress.getText().toString();
        setUpListItemClickListener();

        TName =  root.findViewById((R.id.parcelIdAndName));
       info =  root.findViewById((R.id.warning));

        buttonCheckboxOffer = root.findViewById(R.id.checkboxOffer);
        buttonCheckboxPickUP = root.findViewById(R.id.checkboxPickUP);

        buttonClose =  root.findViewById(R.id.buttonCLOSE);
        //buttonClose2 =  root.findViewById(R.id.buttonCLOSE2);
        //buttonClose3 =  view.findViewById(R.id.buttonCLOSE3);
        buttonMail = root.findViewById(R.id.buttonMAIL);
        buttonCall = root.findViewById(R.id.buttonCALL);
        buttonSms = root.findViewById(R.id.buttonSMS);
        //buttonConfirm = view.findViewById(R.id.confirm);
        //deliverdDetails =  view.findViewById(R.id.deliverdDetailsLinearLayout);
        openDetails=  root.findViewById(R.id.detailsDrive);
       // RecievedOrOnItsWayLinearLayout =  view.findViewById(R.id.RecievedOrOnItsWayLinearLayout);
        searchview = root.findViewById(R.id.filter);
        buttonMail = root.findViewById(R.id.buttonMAIL);

        buttonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(recipientEmailAddress);
            }
        });

        buttonSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms(recipientPhoneNumber);
            }
        });

        buttonCheckboxPickUP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                {
                    buttonView.setEnabled(false);

                    buttonCall.setEnabled(true);
                    buttonCall.setTextColor(R.color.Black);

                    buttonMail.setEnabled(true);
                    buttonMail.setTextColor(R.color.Black);

                    buttonSms.setEnabled(true);
                    buttonSms.setTextColor(R.color.Black);

                    friendsParcelsViewModel.offedToPickedUpParcel(p,userEmail);
                }

            }
        });

        buttonCheckboxOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked())
                {
                    buttonView.setEnabled(false);
                    Boolean flag=true;
                    for (HashMap.Entry<String, Boolean> entry : p.getMyHashMap().entrySet()) {
                        if (isEmailTheSameAsUserEmail(entry.getKey())) {//he ooferd to pick up+ recipient approved him
                            flag = false;
                        }
                    }

                    if (flag) // he is added already no need to add him
                        friendsParcelsViewModel.addPotentialDeliveryMan(p,userEmail);

                }

            }
        });


        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCustomer(recipientPhoneNumber);
            }
        });


       /* buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                friendsParcelsViewModel.receivedParcel(p,userEmail);

            }
        });*/


        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDetails.setVisibility(View.INVISIBLE);
                //updat screen layout so the list view shold take up the whole layout
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) openDetails.getLayoutParams();
                params.weight = 0f;
                openDetails.setLayoutParams(params);
                //   deliverdDetails.setVisibility(View.INVISIBLE);
              //  RecievedOrOnItsWayLinearLayout.setVisibility(View.INVISIBLE);

            }
        });

        //listView.setTextFilterEnabled(true);

      /*  buttonClose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDetails.setVisibility(View.INVISIBLE);
                deliverdDetails.setVisibility(View.INVISIBLE);
                //RecievedOrOnItsWayLinearLayout.setVisibility(View.INVISIBLE);
            }
        });


        buttonClose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDetails.setVisibility(View.INVISIBLE);
                deliverdDetails.setVisibility(View.INVISIBLE);
              //  RecievedOrOnItsWayLinearLayout.setVisibility(View.INVISIBLE);
            }
        });
*/

      final SearchView  searchView1=searchview;
      Button filter=(Button) root.findViewById(R.id.fillterButton);
      filter.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              searchView1.setQuery(searchView1.getQuery(), true);

          }
      });


        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // We're deleting char so we need to reset the adapter data
                itemArrayAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

        });



        return root;

    }

    private void setUpListItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //@SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                p = itemList.get(position);

                recipientPhoneNumber = p.getPhoneNumber();
                recipientEmailAddress = p.getEmail();
                TName.setText("Parcel ID: " + p.getId() + "\nRecipient: " + p.getFirstName() + " " + p.getLastName() + "\n" + "PickUp from: " + p.getLocation() + "\nDeliver To: " + p.getAddress());

                /*// checking waht window to open up
                if (p.getParcelStatus() == ParcelStatus.ON_ITS_WAY || p.getParcelStatus() == ParcelStatus.RECEIVED) {
                    openDetails.setVisibility(View.INVISIBLE);
                    RecievedOrOnItsWayLinearLayout.setVisibility(View.VISIBLE);
                    info.setText("Parcel " + p.getId() + "for " + p.getFirstName() + " " + p.getLastName() + "\n" + "Status: " + p.getParcelStatus().toString() + "\n" + "Delivery Man Name: " + p.getDeliveryManName());
                } else {*/

                 //   RecievedOrOnItsWayLinearLayout.setVisibility(View.INVISIBLE);
                openDetails.setVisibility(View.VISIBLE);

                //will make the open deatails screen take up th whole screen
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) openDetails.getLayoutParams();
                params.weight = 20f;
                openDetails.setLayoutParams(params);

                    info.setText("will be enabled only if recipient approves you to pick up parcel:");


                    if (p.getParcelStatus() == ParcelStatus.SENT) {
                        buttonCheckboxOffer.setEnabled(true);
                        buttonCheckboxOffer.setChecked(false);
                        buttonCheckboxPickUP.setEnabled(false);

                    } else {//status==offerd to pick up
                        //iterate through hashmap
                        for (HashMap.Entry<String, Boolean> entry : p.getMyHashMap().entrySet()) {
                            buttonCheckboxOffer.setEnabled(true);
                            buttonCheckboxOffer.setChecked(false);
                            buttonCheckboxPickUP.setEnabled(false);

                            if (isEmailTheSameAsUserEmail(entry.getKey()) ) {//he ooferd to pick up+ recipient approved him
                                buttonCheckboxOffer.setChecked(true);
                                buttonCheckboxOffer.setEnabled(false);

                                if(entry.getValue()==true)
                                    buttonCheckboxPickUP.setEnabled(true);
                                else
                                    buttonCheckboxPickUP.setEnabled(false);

                            }
                        }
                    }
                //}
            }
        });
    }






        public boolean isEmailTheSameAsUserEmail(String delivaryManEmail) {
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String userEmail=user.getEmail();
            String cutUserEmail=userEmail.split("@")[0];//beacuse we cut the hash email, so we need to make them "even"
            cutUserEmail=cutUserEmail.toLowerCase().trim().replaceAll("\\s+", " ");
            delivaryManEmail=delivaryManEmail.toLowerCase().trim().replaceAll("\\s+", " ");
            boolean even=delivaryManEmail.equals(cutUserEmail);
            if (even)
                return true;
            else
                return false;

        }
    //send email parcel recipient
    public void sendEmail(String recipientAddress){
        //String recipientAddress=item.getEmail();

        String subject="Message From Your Post Man";
        String message="Hey I'm gonna be Your post man, just wanted to say Hi!";

        Intent sendIntent=new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL,recipientAddress);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT,message);

        sendIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(sendIntent,"choose email client"));
    }


    //send SMS parcel recipient
    private void sendSms(String phoneNumber)
    {
        //String phoneNumber=item.getEmail();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        intent.putExtra("Hey I'm gonna be Your post man, just wanted to say Hi!", phoneNumber);
        startActivity(intent);
    }


    //call parcel recipient
    final int CALL_PERMEATION_REQUEST_CODE=1;

    public void callCustomer(String recipientPhoneNumber){
        //String recipientPhoneNumber=item.getEmail();

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+recipientPhoneNumber));
        startActivity(callIntent);

    }


    }





/*

<LinearLayout
        android:id="@+id/RecievedOrOnItsWayDetails"
                android:layout_width="match_parent"
                android:layout_height="8dp"
                android:gravity="center_horizontal"
                android:layout_weight="0.5"
                android:orientation="vertical"
                android:visibility="visible"
                tools:ignore="MissingConstraints">


<LinearLayout
            android:id="@+id/RecievedOrOnItsWayLinearLayout"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:layout_weight="1">


<TextView
                android:id="@+id/info"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="16dp"
                        android:textAppearance="?attr/textAppearanceListItem"
                        android:text="Confirm that parcel was delivered to recipient successfully:"></TextView>



<Button
                android:id="@+id/buttonCLOSE3"
                        android:layout_width="81dp"
                        android:layout_height="40dp"
                        android:layout_marginBottom="16dp"
                        android:layout_marginLeft="16dp"
                        android:text="Back"/>

</LinearLayout>

</LinearLayout>
<LinearLayout
        android:id="@+id/deliverdDetails"
                android:layout_width="match_parent"
                android:layout_height="8dp"
                android:gravity="center_horizontal"
                android:layout_weight="0.5"
                android:orientation="vertical"
                android:visibility="invisible"
                tools:ignore="MissingConstraints">


<LinearLayout
            android:id="@+id/deliverdDetailsLinearLayout"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:layout_weight="1">


<TextView
                android:id="@+id/confirmText"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="16dp"
                        android:textAppearance="?attr/textAppearanceListItem"
                        android:text="Confirm that parcel was delivered to recipient successfully:"></TextView>


<Button
                android:id="@+id/confirm"
                        android:layout_width="200dp"
                        android:layout_height="38dp"
                        android:layout_gravity="center"
                        android:onClick="sendSMS"
                        android:text="CONFIRM"
                        android:textColor="@android:color/black"
                        android:textSize="12sp"
                        android:textStyle="bold" />
<Button
                android:id="@+id/buttonCLOSE2"
                        android:layout_width="81dp"
                        android:layout_height="40dp"
                        android:layout_marginBottom="16dp"
                        android:layout_marginLeft="16dp"
                        android:text="Back"/>

</LinearLayout>

</LinearLayout>*/