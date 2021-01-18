package com.example.spm2.UI.FragmentsPlusViewModels.RegisteredParcels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spm2.Data.ParcelRepository;
import com.example.spm2.Entities.Parcel;

import java.util.List;

public class RegisteredParcelsViewModel extends ViewModel {


    ParcelRepository iParcelRepository;
    private LiveData<List<Parcel>> allRecipientParcelsThatWhereNotReceivedYet;

    public RegisteredParcelsViewModel() {
         iParcelRepository = new ParcelRepository();

    }


    public boolean receivedParcel(Parcel parcel) {
        return iParcelRepository.receivedParcel(parcel);
    }

    public boolean approvePotentialDeliveryMan(Parcel parcel, String posManEmail) {
        return iParcelRepository.approvePotentialDeliveryMan(parcel,posManEmail);
    }

    public LiveData<List<Parcel>> getAllClientParcelsWaiting() {
        allRecipientParcelsThatWhereNotReceivedYet=iParcelRepository.getAllRecipientParcelsThatWhereNotReceivedYet();
        return allRecipientParcelsThatWhereNotReceivedYet;
    }
}