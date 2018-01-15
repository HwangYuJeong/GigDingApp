package org.androidtown.gigdingapp;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.gigdingapp.R;

import java.util.Calendar;

public class MapFragmentDetail extends Fragment implements View.OnClickListener{

    // 동적 레이아웃 생성 시 필요한 변수
    int nTR = 1000; //TableRow
    int nSP = 2000; //Spinner
    int nBT = 3000; //Button
    int iTeamStackCnt = 0;  //팀원 추가버튼 스택 건수
    int iTeamCnt = 0;       //팀원 추가된 현재건수

    FragmentActivity at;
    String[] arrayTmp;              //팀원 array 정보
    ArrayAdapter<String> adapter;   //팀원 정보 adapter

    ViewGroup mapDetailView;
    TableLayout teamTableLy;
    EditText proNameEv;     //프로젝트명 EditView
    EditText proDetailEv;   //프로젝트 상세 EditView


    public MapFragmentDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        mapDetailView = (ViewGroup) inflater.inflate(R.layout.fragment_map_detail, container, false);

        //init();

        return mapDetailView;
    }

    public void onStart() {
        super.onStart();
        AppCompatActivity AppCompat = (AppCompatActivity)getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_map_detail));
        AppCompat.findViewById(R.id.fab).setVisibility(View.INVISIBLE);
    }

    private void init() {

        at = getActivity();
        arrayTmp = getResources().getStringArray(R.array.team);
        teamTableLy = (TableLayout) mapDetailView.findViewById(R.id.teamTableLy);
        adapter = new ArrayAdapter<String>(at, android.R.layout.simple_spinner_item, arrayTmp);

        mapDetailView.findViewById(R.id.teamAddBtn).setOnClickListener(this);
        mapDetailView.findViewById(R.id.Map_savBtn).setOnClickListener(this);
        proNameEv = mapDetailView.findViewById(R.id.proNameEv);
        proDetailEv = mapDetailView.findViewById(R.id.proNameDetailEv);

        Bundle args = getArguments();
        if (args != null) {
            Toast.makeText(at, "proNo = " + args.getString("proNo"), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 팀원 정보 동적 레이아웃 생성
     */
    private void addView() {

        int width250 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        int width120 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());

        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(width250, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(width120, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        lp1.setMargins(50, 0, 50, 0);
        lp2.setMargins(50, 0, 50, 0);

        TableRow tableRow = new TableRow(at);
        TextView textView = new TextView(at);
        Spinner sp = new Spinner(at);
        Button bt = new Button(at);

        tableRow.setId(nTR + iTeamStackCnt);
        //tableRow.setBackground(getResources().getDrawable(R.drawable.border));

        textView.setLayoutParams(lp1);
        textView.setText("팀원");
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);

        sp.setAdapter(adapter);
        sp.setId(nSP + iTeamStackCnt);

        bt.setText("삭제");
        bt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        bt.setLayoutParams(lp2);
        bt.setId(nBT + iTeamStackCnt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iCnt = Integer.parseInt(String.valueOf(v.getId()).substring(1));
                TableRow tbR = (TableRow) mapDetailView.findViewById(nTR + iCnt);
                tbR.removeAllViews();
                iTeamCnt = iTeamCnt - 1;
            }
        });

        tableRow.addView(textView);
        tableRow.addView(sp);
        tableRow.addView(bt);

        teamTableLy.addView(tableRow);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.teamAddBtn:
                addView();
                iTeamStackCnt = iTeamStackCnt + 1;
                iTeamCnt = iTeamCnt + 1;
                break;
            case R.id.Map_savBtn:
                saveContents();
                break;
            case R.id.Map_cancelBtn:
                //뒤로가기
                break;
            default:
                break;
        }
    }

    /**
     * 저장버튼 클릭 시
     */
    private void saveContents() {

        //프로젝트 정보 입력값 체크
        if ("".equals(String.valueOf(proNameEv.getText()))) {
            Toast.makeText(at, "프로젝트 명을 입력하세요", Toast.LENGTH_SHORT).show();
//            proNameEv.requestFocus();
            return;
        }

        if ("".equals(String.valueOf(proDetailEv.getText()))) {
            Toast.makeText(at, "프로젝트 상세를 입력하세요", Toast.LENGTH_SHORT).show();
//            proDetailEv.requestFocus();
            return;
        }

        //팀원 정보 입력값 체크
        String sTmp = "";
        if (iTeamCnt > 0) {
            for (int idx = 0; idx < iTeamStackCnt + 1; idx++) {
                Spinner sp = (Spinner) mapDetailView.findViewById(nSP + idx);
                if (sp != null) {
                    sTmp += String.valueOf(sp.getSelectedItem()) + ",";
                }
            }

            if (sTmp != null || "".equals(sTmp)) {
                sTmp = sTmp.substring(0, sTmp.length() - 1);
                Toast.makeText(at, "팀원은 = " + sTmp, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(at, "팀원을 선택해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send Post Data To PHP

    }
}
