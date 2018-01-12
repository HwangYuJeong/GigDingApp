package org.androidtown.gigdingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.androidtown.gigdingapp.common.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleFragment extends Fragment implements ListView.OnItemClickListener {

    ArrayList arraySchList = new ArrayList();
    ViewGroup schView;
    MyAdapter adapter;
    ListView listV;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        if (schView == null) {
            schView = (ViewGroup) inflater.inflate(R.layout.fragment_schedule, container, false);
            listV = (ListView) schView.findViewById(R.id.SchListView);
            adapter = new MyAdapter(getActivity(), R.layout.sch_list_row, arraySchList, 3);
            listV.setOnItemClickListener(this);
        } else {
            ViewGroup parentViewGroup = (ViewGroup) schView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(schView);
            }
        }
        return schView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("PJH", "onActivityCreated");
        // 타이틀 SET

        AppCompatActivity AppCompat = (AppCompatActivity) getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_schedule));
        AppCompat.findViewById(R.id.fab).setVisibility(View.VISIBLE);

        putArrayLIst();
        listV.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap hashMap = new HashMap((HashMap) adapter.getItem(position));

        Bundle args = new Bundle();
//        args.putString("proNo", (String) hashMap.get(0));
//        args.putString("proName", (String) hashMap.get(1));
//        args.putString("teamCnt", (String) hashMap.get(2));

        Fragment fragemnt = new ScheduleFragmentDetail();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragemnt.setArguments(args);
        ft.replace(R.id.content_fragment_layout, fragemnt).addToBackStack(null).commit();
    }

    private void putArrayLIst() {
        arraySchList.clear();
        for (int idx = 0; idx < 15; idx++) {
            HashMap map = new HashMap();
            map.put(0, "2018-01-01 " + idx);
            map.put(1, "타이틀  " + idx);
            map.put(2, "기간    " + idx);

            arraySchList.add(map);
        }
    }

}
