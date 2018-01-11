package org.androidtown.gigdingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.androidtown.gigdingapp.common.MyAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleFragment extends Fragment implements ListView.OnItemClickListener {

    ArrayList arrayProjectList = new ArrayList();
    ViewGroup schView;
    MyAdapter adapter;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        schView = (ViewGroup) inflater.inflate(R.layout.fragment_schedule, container, false);

        for (int idx = 0; idx < 15; idx++) {
            HashMap map = new HashMap();
            map.put(0, "2018-01-01");
            map.put(1, "타이틀  ");
            map.put(2, "기간    ");

            arrayProjectList.add(map);
        }

        adapter = new MyAdapter(getActivity(), R.layout.sch_list_row, arrayProjectList, 3);

        ListView listV = (ListView) schView.findViewById(R.id.SchListView);
        listV.setOnItemClickListener(this);
        listV.setAdapter(adapter);
        return schView;
    }


    public void onStart() {
        super.onStart();
        // 타이틀 SET
        AppCompatActivity AppCompat = (AppCompatActivity) getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_schedule));
        AppCompat.findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap hashMap = new HashMap((HashMap) adapter.getItem(position));

        Bundle args = new Bundle();
        args.putString("proNo", (String) hashMap.get(0));
        args.putString("proName", (String) hashMap.get(1));
        args.putString("teamCnt", (String) hashMap.get(2));

        Fragment fragemnt = new ProFragmentDetail();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragemnt.setArguments(args);
        ft.replace(R.id.content_fragment_layout, fragemnt).addToBackStack(null).commit();
    }
}
