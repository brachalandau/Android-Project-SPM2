package com.example.spm2.Utils;


public interface NotifyDataChange<T> { //to listen to the updates of the information in the fireBase
    void OnDataChanged(T obj);
    void onFailure(Exception exception);
}
