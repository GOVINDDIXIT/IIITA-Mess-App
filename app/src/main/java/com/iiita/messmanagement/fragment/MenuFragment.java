package com.iiita.messmanagement.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iiita.messmanagement.CustomAdapter;
import com.iiita.messmanagement.R;

import me.relex.circleindicator.CircleIndicator;


public class MenuFragment extends Fragment {

    private CustomAdapter customAdapter;
    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        // Inflate the layout for this fragment
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.menu_view_pager);
        int[] layouts = {R.layout.monday_menu,R.layout.tuesday_menu,R.layout.wednesday_menu,R.layout.thursday_menu,R.layout.friday_menu,R.layout.saturday_menu,R.layout.sunday_menu};
        customAdapter = new CustomAdapter(layouts,getContext());
        viewpager.setAdapter(customAdapter);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(customAdapter);
        indicator.setViewPager(viewpager);
        return view;
    }

}
