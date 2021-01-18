package com.example.spm2.Entities;


import android.location.Location;

import com.example.spm2.Utils.ParcelStatus;
import com.example.spm2.Utils.ParcelType;
import com.example.spm2.Utils.ParcelWeight;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Parcel {

    String id;
    ParcelType parcelType;
    boolean fragile;
    ParcelWeight parcelWeight;
    String location;
    String firstName;
    String lastName;
    String address;
    String sentDate;
    String sentTime;
    String receivedDate;
    String receivedTime;
    String PhoneNumber;
    String email;
    ParcelStatus parcelStatus=ParcelStatus.SENT;
    String deliveryManName="NO";
    HashMap<String, Boolean> myHashMap;
    private Date whenLoadToFirebase;



    ///////////////////////////////////// constructor////////////////////////////


    public Parcel(String id,ParcelType parcelType, boolean fragile, ParcelWeight parcelWeight, String location, String firstName, String lastName, String address,String sendDate, String receivedDate, String sentTime, String receivedTime, String phoneNumber, String email) {
        this.id=id;
        this.parcelType = parcelType;
        this.fragile = fragile;
        this.parcelWeight = parcelWeight;
        this.location = location;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.sentDate = sentDate;
        this.sentTime = sentTime;
        this.receivedDate = receivedDate;
        this.receivedTime = receivedTime;
        this.PhoneNumber = phoneNumber;
        this.email = email;
        this.myHashMap=new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,-10);
        this.whenLoadToFirebase= calendar.getTime();

    }

    public Parcel() {
        this.id=id;
        this.parcelType = ParcelType.ENVELOPE;
        this.fragile = true;
        this.parcelWeight = ParcelWeight.UP_TO_1_KGRAM;
        this.location ="";
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.sentDate = "";
        this.sentTime = "";
        this.receivedDate = "";
        this.receivedTime = "";
        PhoneNumber = "";
        this.email = "";
        this.parcelStatus = ParcelStatus.SENT;
        this.deliveryManName = "";
        this.myHashMap=new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,-10);
        this.whenLoadToFirebase= calendar.getTime();

    }


    ///////////////////////////////////// get/set ////////////////////////////


    public ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    public ParcelWeight getParcelWeight() {
        return parcelWeight;
    }

    public void setParcelWeight(ParcelWeight parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSendDate() {
        return sentDate;
    }

    public void setSendDate(String sendDate) {
        this.sentDate = sendDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ParcelStatus getParcelStatus() {
        return parcelStatus;
    }

    public void setParcelStatus(ParcelStatus parcelStatus) {
        this.parcelStatus = parcelStatus;
    }

    public String getDeliveryManName() {
        return deliveryManName;
    }

    public void setDeliveryManName(String deliveryManName) {
        this.deliveryManName = deliveryManName;
    }

   public HashMap<String, Boolean> getMyHashMap() {
        return myHashMap;
    }

    public int getMyHashMapSize() {
        return myHashMap.size();
    }

    public void setMyHashMap(HashMap<String, Boolean> myHashMap) {
        this.myHashMap = myHashMap;
    }

    public void addMyHashMap(String s, Boolean b ) {
        this.myHashMap.put(s,b);
    }

    public void clearMyHashMap( ) {
        this.myHashMap.clear();
    }

    public void setValueToTrueInMyHashMap(String email ) {
        for (HashMap.Entry<String, Boolean> entry : myHashMap.entrySet()) {
            if(entry.getKey()==email)
                entry.setValue(true);

        }
    }
    public Date getWhenLoadToFirebase() {
        return whenLoadToFirebase;
    }

    public void setWhenLoadToFirebase(Date whenLoadToFirebase) {
        this.whenLoadToFirebase = whenLoadToFirebase;
    }

}
