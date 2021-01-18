package com.example.spm2.UI.FragmentsPlusViewModels.RegisteredParcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.spm2.Entities.Parcel;
import com.example.spm2.R;

import java.util.ArrayList;
import java.util.List;

public class RegisteredParcelsFragment extends Fragment {






    RegisteredParcelsViewModel registeredParcelsViewModel;
    private ListView listView;
    View view;

    public static List<Parcel> itemList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registered_parcels, container, false);
        final ArrayList<Parcel> itemList = new ArrayList<>();
        registeredParcelsViewModel = ViewModelProviders.of(this).get(RegisteredParcelsViewModel.class);
        listView = (ListView)root.findViewById(R.id.item_list);
        final RegisterdArrayAdapter registerdArrayAdapter = new RegisterdArrayAdapter(this.getActivity(),itemList,registeredParcelsViewModel);
        listView.setAdapter(registerdArrayAdapter);

        registeredParcelsViewModel.getAllClientParcelsWaiting().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
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
                    registerdArrayAdapter.notifyDataSetChanged();
                }
            }
        });



        return root;
    }
}



/*@Override
                    public void onChanged(List<Parcel> parcelList) {
                        boolean listChanged = false;
                        for (Parcel parcel: parcelList)
                        {
                            itemList.add(parcel);
                            listChanged = true;
                        }
                        if(listChanged)
                            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged(); }*/