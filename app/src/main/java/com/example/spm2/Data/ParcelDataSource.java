package com.example.spm2.Data;


import androidx.annotation.NonNull;

import com.example.spm2.Entities.Parcel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParcelDataSource implements IParcelDataSource {

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);
        void onFailure(Exception exception);
    }
    ParcelDataSource(){
    }

    static DatabaseReference parcelsRef;//points on the root of the list of drives
    static List<Parcel> parcelsList;
    static List<Parcel> parcel_SENT_List;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        parcelsRef = database.getReference("parcels");
        parcelsList = new ArrayList<>();
        parcel_SENT_List = new ArrayList<>();
    }

    //a listener to the fireBase
    private static ChildEventListener driverRefChildEventListener;


    //adds the parcel in the fire base
    public void addParcelToFireBase(final Parcel parcel) {
        String key = parcel.getId();
        stopNotifyToParcelList();

        //Puts the parcel in the appropriate place.
        //After adding, wait to see what the answer is by the listener
        parcelsRef.child(key).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                parcelRefValueEventListener=tmp;
                parcelsRef.addValueEventListener(parcelRefValueEventListener);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                parcelRefValueEventListener=tmp;
                parcelsRef.addValueEventListener(parcelRefValueEventListener);
            }
        });
    }



    //upDates the parcel in the fire base
    public boolean updateParcelInFireBase(final Parcel toUpdate) {
        final boolean[] ret = {false};

        parcelsRef.child(toUpdate.getId()).setValue(toUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ret[0]=true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ret[0]=false;
            }
        });
        return  ret[0];
    }


    //removes the parcel in the fire base
    public boolean removeParcelFromFireBase(final Parcel parcel) {
        final String key = parcel.getId();
        final boolean[] ret = {false};
        parcelsRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ret[0] =true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ret[0]=false;
            }
        });
        return ret[0];
    }



    private static ValueEventListener parcelRefValueEventListener,tmp;
    public static void notifyToParcelList(final NotifyDataChange<List<Parcel>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (parcelRefValueEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify parcel list"));
                return;
            }
            parcelsList.clear();
            parcelRefValueEventListener =new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    parcelsList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Parcel parcel = data.getValue(Parcel.class);
                        parcelsList.add(parcel);
                    }
                    notifyDataChange.OnDataChanged(parcelsList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            parcelsRef.addValueEventListener(parcelRefValueEventListener);
        }
    }

    public static void stopNotifyToParcelList() {
        if (parcelRefValueEventListener != null) {
            parcelsRef.removeEventListener(parcelRefValueEventListener);
            tmp=parcelRefValueEventListener;
            parcelRefValueEventListener = null;
        }
    }

    /* public LiveData<List<Parcel>> allParcels() { //returns the list of all "SENT" parcels (sent=sent to warehouse-meaning no one picked the parcel up yet)
        notifyToParcelList(new NotifyDataChange<List<Parcel>>() {
            @Override
            public void OnDataChanged(List<Parcel> obj) {

                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String userEmail=user.getEmail();
                for (Parcel parcel:obj) {
                    //checks: status=sent +makes sure to return only current users parcels
                    if ((parcel.getParcelStatus() == ParcelStatus.SENT ||parcel.getParcelStatus() == ParcelStatus.OFFERED_TO_PICK_UP) && parcel.getEmail()==userEmail) {
                        if (!(parcel_SENT_List.contains(parcel))) {
                            parcel_SENT_List.add(parcel);
                        }
                    }
                }
                parcel_SENT_List = obj;
            }

            @Override
            public void onFailure(Exception exception) {
                //Toast.makeText(getBaseContext(), "error to get drives list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
        return (LiveData<List<Parcel>>) parcel_SENT_List;

    }*/


}