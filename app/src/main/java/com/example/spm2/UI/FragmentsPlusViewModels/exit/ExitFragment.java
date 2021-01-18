package com.example.spm2.UI.FragmentsPlusViewModels.exit;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spm2.R;


public class ExitFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.exit(0);



        View root = inflater.inflate(R.layout.fragment_exit, container, false);
        return root;
    }
}