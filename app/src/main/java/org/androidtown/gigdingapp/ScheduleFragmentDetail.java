package org.androidtown.gigdingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import org.androidtown.gigdingapp.common.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleFragmentDetail extends Fragment {

    ViewGroup schDetailView;

    public ScheduleFragmentDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        if(schDetailView == null) {
            schDetailView = (ViewGroup) inflater.inflate(R.layout.fragment_sch_detail, container, false);

            Bundle args = getArguments();
            if (args != null) {
                Toast.makeText(getActivity(), "proNo = " + args.getString("proNo"), Toast.LENGTH_SHORT).show();
            }
        } else {
            ViewGroup parentViewGroup = (ViewGroup) schDetailView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(schDetailView);
            }
        }
        return schDetailView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity AppCompat = (AppCompatActivity)getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_schedule_detail));
        AppCompat.findViewById(R.id.fab).setVisibility(View.INVISIBLE);

    }
}
