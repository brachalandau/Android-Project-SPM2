package com.example.spm2.Data;

import androidx.lifecycle.LiveData;

import com.example.spm2.Entities.Parcel;

import java.util.List;

public interface IParcelRepository {
    //for friends fragment
    LiveData<List<Parcel>> getAllRecipientParcelsThatWhereNotReceivedYet();

    //for registered fragment
    LiveData<List<Parcel>> getAllParcelsThatWhereNotYetPickedUp();

    //delivery man sets status to ON_ITS_WAY, and delivary man name to his email
    void offedToPickedUpParcel(final Parcel parcel,final String postManEmail);

    //recipient updates parcel status to recieved
    boolean receivedParcel(final Parcel parcel);

    //delivery man offers to pick up, but recipient did not approve yet
    void addPotentialDeliveryMan(final Parcel parcel, final String email);

    //recipient approves this delivery man to deliver his parcel
    public boolean approvePotentialDeliveryMan(final Parcel parcel, final String email);
}

