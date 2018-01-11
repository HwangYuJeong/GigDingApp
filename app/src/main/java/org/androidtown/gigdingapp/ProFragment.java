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

public class ProFragment extends Fragment {

    ArrayList arrayProjectList = new ArrayList();
    ViewGroup proView;

    public ProFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        proView = (ViewGroup) inflater.inflate(R.layout.fragment_pro, container, false);

        for (int idx = 0; idx < 15; idx++) {
            HashMap map = new HashMap();
            map.put(0, "프로젝트 " + String.valueOf(idx));
            map.put(1, "프로젝명 " + String.valueOf(idx));
            map.put(2, String.valueOf(idx) + "명");

            arrayProjectList.add(map);
        }
        final MyAdapter adapter = new MyAdapter(getActivity(), R.layout.project_list_row, arrayProjectList, 1);
        ListView listV = (ListView) proView.findViewById(R.id.ProListView);

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
        listV.setAdapter(adapter);

        return proView;
    }

    public void onStart() {
        super.onStart();
        // 타이틀 SET
        AppCompatActivity AppCompat = (AppCompatActivity) getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_project));
        AppCompat.findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }
}
