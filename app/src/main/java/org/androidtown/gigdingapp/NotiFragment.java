package org.androidtown.gigdingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NotiFragment extends Fragment {

    private ListView listView;

    //private ListView lv;

    public NotiFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noti, container, false);

        listView= (ListView) view.findViewById(R.id.listView);

        final String[] strarrListItem = new String[100]; //리스트뷰에 뿌릴 문자열 배열
        for (int i = 0; i < 100; i++) {
            strarrListItem[i] = "ListViewItem_" + i;
            Log.d("담기는거 확인:", strarrListItem[i]);
        }
        ArrayAdapter<String> adtArray = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, strarrListItem);
        listView.setAdapter(adtArray);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("클릭되나","확인이요");
            }
        });

            //리스트 뷰와 연결할 ArrayAdapter 생성
        /*    ArrayAdapter<String> adtArray = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strarrListItem);
            this.lv = (ListView) view.findViewById(R.id.listView);
            this.lv.setAdapter(adtArray); //포인트 : 리스트뷰와 어래이 어댑터를 연결하여 배열에 있는 문자열들을 출력*/


            // Inflate the layout for this fragment*/
            return view;

    }
}