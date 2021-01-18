package com.example.spm2.UI.FragmentsPlusViewModels.RegisteredParcels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.spm2.Entities.Parcel;
import com.example.spm2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterdArrayAdapter extends ArrayAdapter<Parcel> {

    Context context;
    private int listItemLayout;
    RegisteredParcelsViewModel registeredParcelsViewModel;
    public RegisterdArrayAdapter(Context context1, List<Parcel> itemList, RegisteredParcelsViewModel registeredParcelsViewModel1) {
        super(context1 ,R.layout.lv_item, itemList);
        listItemLayout = R.layout.lv_item;
        context=context1;
        registeredParcelsViewModel=registeredParcelsViewModel1;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Parcel parcel = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayout, parent, false);

            TextView info= (TextView) convertView.findViewById(R.id.parcelInfo);
            viewHolder.parcelId =info;

            //set an alert dialog, that will allow to choose what carrier to approve, when button is clicked
            Button choose= (Button) convertView.findViewById(R.id.approveCarriers);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    final String carrierList[]=fillCarrierList(parcel);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Tap a carrier to approve him:");
                    builder.setItems(carrierList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {

                            registeredParcelsViewModel.approvePotentialDeliveryMan(parcel,carrierList[position]);//approve carrier
                            Toast.makeText(context, "carrier "+carrierList[position]+" was approved", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setPositiveButton("Close",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    //Do nothing here because we override this button later to change the close behaviour.
                                    //However, we still need this because on older versions of Android unless we
                                    //pass a handler the button doesn't get instantiated
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                    //doing this so that the dialog dosent close immeadeatly after only one carrier is choosen
                    //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                                alert.dismiss();
                        }
                    });

                }
            });

            viewHolder.chooseCarrierButton =choose;

            //when button is clicked it will cause the parcels status to change to RECEIVED
            Button received = (Button) convertView.findViewById(R.id.parcelReceived);
            received.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSucceed=registeredParcelsViewModel.receivedParcel(parcel);//set status

                    final Toast toast=Toast.makeText(context, "parcel "+parcel.getId()+" 's status was updated to received\nWe Hope You Enjoyed your experience with SPM!", Toast.LENGTH_LONG);
                    toast.show();
                    //this will make the toast appear for longer
                    new CountDownTimer(9000, 1000)
                    {

                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.show();}

                    }.start();


                }
            });
            viewHolder.parcelReceivedButton=received;


            convertView.setTag(viewHolder); // view lookup cache stored in tag
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // Populate the data into the template view using the data object
        viewHolder.parcelId.setText(parcel.getId());

        // Return the completed view to render on screen
        return convertView;
    }

    //fill carrier list with pos man's that offerd to pick up the parcel
    private String[] fillCarrierList(Parcel parcel) {
        List<String> carrierList=new ArrayList<String>();
        for (HashMap.Entry<String, Boolean> entry : parcel.getMyHashMap().entrySet()) {
            carrierList.add(cutEmail(entry.getKey()));
        }
        return carrierList.toArray(new String[0]);

    }



    // need to cut off the @gmail.com, because a hash map cant store the @ char
    private String cutEmail(String postManEmail) {
        String result = postManEmail.split("@")[0];
        return result;
    }

    private static class ViewHolder {
        TextView parcelId;
        Button chooseCarrierButton;
        Button parcelReceivedButton;
    }


}




