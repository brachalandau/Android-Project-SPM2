package com.example.spm2.UI.FragmentsPlusViewModels.FriendsParcels;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.spm2.Entities.Parcel;
import com.example.spm2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.spm2.UI.FragmentsPlusViewModels.FriendsParcels.FriendsParcelsFragment.postManAddress;

public class ItemArrayAdapter extends ArrayAdapter<Parcel> implements Filterable {

    private int listItemLayout;
    private Filter ParcelsFilter;
    private List<Parcel> parcelList;
    private List<Parcel> origParcelList;
    //private List<Parcel> parcelList2;
    View friendFragmentView;

    static public Context helpContext;


    public ItemArrayAdapter(Context context, List<Parcel> itemList,View friendFragmentView1) {
        super(context,R.layout.item_list, itemList);
        listItemLayout = R.layout.item_list;
        this.parcelList = itemList;
        this.origParcelList = itemList;
        //this.parcelList2 = itemList;
        helpContext=context;
        friendFragmentView=friendFragmentView1;

    }




    //creating the Adapter so viewers could see it
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayout, parent, false);
            viewHolder.item = convertView.findViewById(R.id.parcelWaiting);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Parcel parcel = getItem(position);
        viewHolder.item.setText(parcel.getFirstName()+"'s parcel, deliver to "+parcel.getAddress());
        return convertView;
    }

    private static class ViewHolder {
        TextView item;
    }

    @Override
    public Filter getFilter() {
            ParcelsFilter = new ParcelFilter();
        return ParcelsFilter;
    }

    public void resetData() {
        parcelList = origParcelList;

    }

    private class ParcelFilter extends Filter {
        FilterResults results = new FilterResults();


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                List<Parcel> newParcelList = new ArrayList<>();
                newParcelList=parcelList;
            }
            else {
                List<Parcel> newParcelList = new ArrayList<>();
                String PMAddress= ((EditText)friendFragmentView.findViewById(R.id.postManAddress)).getText().toString();

                float floatConstraint=Float.parseFloat(constraint.toString());;
                for (Parcel parcel : parcelList) {
                    if (PMAddress=="")
                        PMAddress=parcel.getAddress();//default
                    if (distanceFromAToB(parcel.getAddress(),PMAddress)<=floatConstraint) {
                        newParcelList.add(parcel);
                    }
                }
                results.values = newParcelList;
                results.count = newParcelList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                parcelList=origParcelList;
                notifyDataSetChanged();

            }
            else {
                parcelList.clear();
                parcelList.addAll((List<Parcel>) results.values);
                notifyDataSetChanged();
            }
        }


//////////////////////////////functions///////////////////////////////////

        //converts from String To Location
        public Location fromStringToLocation(String addressStr) {
            //String addressStr = "faisalabad";/// this give me correct address
            Geocoder geoCoder = new Geocoder(helpContext);
            double latitude=0;
            double longtitude=0;

            try {
                List<Address> addresses = geoCoder.getFromLocationName(addressStr, 1);
                if (addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();
                    longtitude = addresses.get(0).getLongitude();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            Location location = new Location("");
            location.setLatitude(latitude);
            location.setLongitude(longtitude);
            return location;
        }


        //converts from Location to String
        public String fromLocationToString(Location location) {
            Geocoder geocoder = new Geocoder(helpContext, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getAddressLine(0);
                    return cityName;
                }

                return "no place: (" + location.getLongitude() + " , " + location.getLatitude() + ")";
            } catch (
                    IOException e)

            {
                e.printStackTrace();
            }
            return "IOException ...";
        }


        //calculates distance from a to b
        public float distanceFromAToB(String a, String b){

            //first convert strings to location
            Location A=fromStringToLocation(a);
            Location B=fromStringToLocation(b);

            float distance = A.distanceTo(B);
            return distance;
        }

    }
}
