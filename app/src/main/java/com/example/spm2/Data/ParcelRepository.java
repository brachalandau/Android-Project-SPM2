package com.example.spm2.Data;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.spm2.Entities.Parcel;
import com.example.spm2.Utils.ParcelStatus;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class ParcelRepository implements IParcelRepository {

    ParcelDataSource parcelDataSource;
    MutableLiveData<List<Parcel>> allRecipientParcelsThatWhereNotReceivedYet;
    MutableLiveData<List<Parcel>> parcelsThatWhereNotYetPickedUp;
    List<Parcel> myParcels;
    List<Parcel> friendParcles;
    Parcel parcel;
    String emailUser;

    private static FirebaseAuth mAuth;

    static
    {
        mAuth = FirebaseAuth.getInstance();
    }

    public ParcelRepository() {

        myParcels=new ArrayList<>();
        friendParcles=new ArrayList<>();
        parcel=new Parcel();
        allRecipientParcelsThatWhereNotReceivedYet=new MutableLiveData<>();
        parcelsThatWhereNotYetPickedUp=new MutableLiveData<>();
        parcelDataSource = new ParcelDataSource();
        emailUser=mAuth.getCurrentUser().getEmail();

        //fill both lists so that they can be observed
        parcelDataSource.notifyToParcelList(new ParcelDataSource.NotifyDataChange <List<Parcel>>() {

            @Override
            public void OnDataChanged(List<Parcel> parcelList) {

                //first fill list for parcelToShipping

                for(Parcel parcel:parcelList){
                    if(parcel.getParcelStatus()==ParcelStatus.SENT ||parcel.getParcelStatus()==ParcelStatus.OFFERED_TO_PICK_UP){//we want to show all parcels that where not picked up already by a delivery man
                        friendParcles.add(parcel);
                    }
                }
                parcelsThatWhereNotYetPickedUp.postValue(friendParcles);

                //now fill list for allRecipientParcelsThatWhereNotReceivedYet
                for(int i=0;i<parcelList.size();i++){
                    parcel=parcelList.get(i);
                    String emailLowerCase=parcel.getEmail().toLowerCase().trim().replaceAll("\\s+", " ");
                    if(emailLowerCase.equals(emailUser)&& parcel.getParcelStatus()!= ParcelStatus.RECEIVED){//checking that the parcels belong to the current user+the parcels where not yet received
                        myParcels.add(parcel);
                    }
                }
                allRecipientParcelsThatWhereNotReceivedYet.postValue(myParcels);
            }
            @Override
            public void onFailure(Exception exception) {
            }
        });
    }

    //for friends fragment
    @Override
    public LiveData<List<Parcel>>getAllRecipientParcelsThatWhereNotReceivedYet() {
        return allRecipientParcelsThatWhereNotReceivedYet;
    }

    //for registered fragment
    @Override
    public LiveData<List<Parcel>> getAllParcelsThatWhereNotYetPickedUp() {

        return parcelsThatWhereNotYetPickedUp;
    }


    //delivery man sets status to ON_ITS_WAY, and delivary man name to his email
    public  void offedToPickedUpParcel(final Parcel parcel,final String postManEmail) {
        final String key = parcel.getId();
        parcel.clearMyHashMap();//delete all offers ,we dont need them any more ,we do it so we wont have problems in SPM1 to add to DAO
        parcel.setParcelStatus(ParcelStatus.ON_ITS_WAY);
        parcel.setDeliveryManName(postManEmail);
        parcelDataSource.updateParcelInFireBase(parcel);

    }

    //recipient updates parcel status to recieved
    public boolean receivedParcel(final Parcel parcel) {
        parcel.clearMyHashMap();//delete all offers ,we dont need them any more ,we do it so we wont have problems in SPM1 to add to DAO, delete here too, cause just in case that recipient recieved it on his own-with out an outside carrier
        parcel.setParcelStatus(ParcelStatus.RECEIVED);
        return parcelDataSource.updateParcelInFireBase(parcel);
    }


    //delivery man offers to pick up, but recipient did not approve yet
    public  void addPotentialDeliveryMan(final Parcel parcel, final String email) {
        String cutEmail=cutEmail(email);// need to cut off the @gmail.com, because a hash map cant store the @ char
        parcel.addMyHashMap(cutEmail,false);
        parcel.setParcelStatus(ParcelStatus.OFFERED_TO_PICK_UP);
        parcelDataSource.updateParcelInFireBase(parcel);
    }


    //recipient approves this delivery man to deliver his parcel
    public boolean approvePotentialDeliveryMan(final Parcel parcel, final String email) {
        String cutEmail=cutEmail(email);// need to cut off the @gmail.com, because a hash map cant store the @ char
        parcel.setValueToTrueInMyHashMap(cutEmail);//update carrier pick up offer to true -meaning he could now pick it up
       return parcelDataSource.updateParcelInFireBase(parcel);

    }

    // need to cut off the @gmail.com, because a hash map cant store the @ char
    private String cutEmail(String postManEmail) {
        String result = postManEmail.split("@")[0];
        return result;
    }

}
