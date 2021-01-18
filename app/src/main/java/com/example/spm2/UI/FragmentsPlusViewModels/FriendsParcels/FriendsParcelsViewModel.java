package com.example.spm2.UI.FragmentsPlusViewModels.FriendsParcels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spm2.Data.IParcelRepository;
import com.example.spm2.Data.ParcelRepository;
import com.example.spm2.Entities.Parcel;

import java.util.List;

public class FriendsParcelsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    IParcelRepository iParcelRepository;

    public FriendsParcelsViewModel() {
        mText = new MutableLiveData<>();
        iParcelRepository=new ParcelRepository();

    }

    public LiveData<List<Parcel>> getAllParcels() {
        return iParcelRepository.getAllParcelsThatWhereNotYetPickedUp();
    }

    public void offedToPickedUpParcel(Parcel parcel,String userEmail) {
        iParcelRepository.addPotentialDeliveryMan(parcel,userEmail);
    }

    public void addPotentialDeliveryMan(Parcel parcel, String userEmail) {
        iParcelRepository.addPotentialDeliveryMan(parcel,userEmail);
    }

    public void receivedParcel(Parcel parcel, String userEmail) {
        iParcelRepository.receivedParcel(parcel);
    }
}



