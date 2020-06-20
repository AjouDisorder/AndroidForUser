package com.example.ttruserver2.detailRestaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ttruserver2.R;

public class InfoFragment extends Fragment {
    private View view;
    private TextView tv_description;
    private TextView tv_address;
    private String description;
    private String address;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info_detail_restaurant, container, false);

        tv_description = view.findViewById(R.id.tv_restaurantDesc);
        tv_address = view.findViewById(R.id.tv_restaurantAddress);

        if(getArguments() != null){
            description = getArguments().getString("description");
            tv_description.setText(description);
            address = getArguments().getString("address");
            tv_address.setText(address);
        }


        return view;
    }
}