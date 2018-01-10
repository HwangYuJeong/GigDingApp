package org.androidtown.gigdingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;

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

        for(int idx=0; idx<15; idx++) {
            HashMap map = new HashMap();
            map.put(0, "프로젝트 " + String.valueOf(idx));
            map.put(1, "프로젝명 " + String.valueOf(idx));
            map.put(2, String.valueOf(idx) + "명");

            arrayProjectList.add(map);
        }
        MyAdapter adapter = new MyAdapter(getActivity());
        ListView listV = (ListView) proView.findViewById(R.id.list_view);
        listV.setOnItemClickListener(adapter);
        listV.setAdapter(adapter);

        return proView;
    }

    public class RowDataViewHolder {
        public TextView proNoTv;
        public TextView proNameTv;
        public TextView teamCntTv;
        public TextView endTimeTv;
    }

    private class MyAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener{
        LayoutInflater lnf;

        public MyAdapter(Activity context) {
            super(context, R.layout.project_list_row, arrayProjectList);
            lnf = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayProjectList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arrayProjectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final RowDataViewHolder viewHolder;
            convertView = lnf.inflate(R.layout.project_list_row, parent, false);
            viewHolder = new RowDataViewHolder();

            viewHolder.proNoTv = (TextView) convertView.findViewById(R.id.proNoTv);
            viewHolder.proNameTv = (TextView) convertView.findViewById(R.id.proNameTv);
            viewHolder.teamCntTv = (TextView) convertView.findViewById(R.id.teamCntTv);
            viewHolder.endTimeTv = (TextView) convertView.findViewById(R.id.endTimeTv);

            HashMap map = (HashMap) arrayProjectList.get(position);

            viewHolder.proNoTv.setText(String.valueOf(map.get(0)));
            viewHolder.proNameTv.setText(String.valueOf(map.get(1)));
            viewHolder.teamCntTv.setText(String.valueOf(map.get(2)));
            viewHolder.endTimeTv.setText(String.valueOf(map.get(3)));

            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("PJH", "position = " + position);
            Log.d("PJH", "getItem = " + getItem(position));

            HashMap hashMap = new HashMap((HashMap) getItem(position));

            Log.d("PJH", "0번  " + hashMap.get(0));
            Log.d("PJH", "1번  " + hashMap.get(1));
            Log.d("PJH", "2번  " + hashMap.get(2));

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

    public void onStart() {
        super.onStart();
        // 타이틀 SET
        AppCompatActivity AppCompat = (AppCompatActivity)getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_project));
        AppCompat.findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }

}
