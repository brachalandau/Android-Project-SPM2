package com.example.spm2.Data;

import com.example.spm2.Entities.Parcel;

public interface IParcelDataSource {

    //upDates the parcel in the fire base
    boolean updateParcelInFireBase(final Parcel toUpdate);

    //removes the parcel in the fire base
    boolean removeParcelFromFireBase(final Parcel parcel);

    //adds the parcel in the fire base
    void addParcelToFireBase(final Parcel parcel);

}



