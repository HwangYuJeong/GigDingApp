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

public class MapFragmentDetail extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    // 동적 레이아웃 생성 시 필요한 변수
    int nTR = 1000; //TableRow
    int nSP = 2000; //Spinner
    int nBT = 3000; //Button
    int iTeamStackCnt = 0;  //팀원 추가버튼 스택 건수
    int iTeamCnt = 0;       //팀원 추가된 현재건수

    int currentYear = 0;        //현재기준 년도
    int currentMonth = 0;       //현재기준 월
    int currentDay = 0;         //현재기준 일
    int datePickerflag = 0;     //달력 flag
    int FLAG_START_DATE = 0;    //시작일 변수
    int FLAG_END_DATE = 1;      //종료일 변수

    FragmentActivity at;
    String[] arrayTmp;              //팀원 array 정보
    ArrayAdapter<String> adapter;   //팀원 정보 adapter

    ViewGroup proDetailView;
    TableLayout teamTableLy;
    EditText proNameEv;     //프로젝트명 EditView
    EditText proDetailEv;   //프로젝트 상세 EditView
    TextView proStartTv;    //프로젝트 시작일 View
    TextView proEndTv;      //프로젝트 종료일 View


    public MapFragmentDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        proDetailView = (ViewGroup) inflater.inflate(R.layout.fragment_map_detail, container, false);

        //init();

        return proDetailView;
    }

    public void onStart() {
        super.onStart();
        AppCompatActivity AppCompat = (AppCompatActivity)getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_project_detail));
        AppCompat.findViewById(R.id.fab).setVisibility(View.INVISIBLE);
    }

    private void init() {

        at = getActivity();
        arrayTmp = getResources().getStringArray(R.array.team);
        teamTableLy = (TableLayout) proDetailView.findViewById(R.id.teamTableLy);
        adapter = new ArrayAdapter<String>(at, android.R.layout.simple_spinner_item, arrayTmp);

        proDetailView.findViewById(R.id.teamAddBtn).setOnClickListener(this);
        proDetailView.findViewById(R.id.savBtn).setOnClickListener(this);
        proDetailView.findViewById(R.id.proStartCal).setOnClickListener(this);
        proDetailView.findViewById(R.id.proEndCal).setOnClickListener(this);

        proStartTv = proDetailView.findViewById(R.id.proStartTv);
        proEndTv = proDetailView.findViewById(R.id.proEndTv);
        proNameEv = proDetailView.findViewById(R.id.proNameEv);
        proDetailEv = proDetailView.findViewById(R.id.proNameDetailEv);

        Bundle args = getArguments();
        if (args != null) {
            Toast.makeText(at, "proNo = " + args.getString("proNo"), Toast.LENGTH_SHORT).show();
        }

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        currentDay = cal.get(Calendar.DAY_OF_MONTH);

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
                TableRow tbR = (TableRow) proDetailView.findViewById(nTR + iCnt);
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
            case R.id.proStartCal:
                datePickerflag = FLAG_START_DATE;                                                              //달력 flag 시작일로 SET
                new DatePickerDialog(at, this, currentYear, currentMonth, currentDay).show();       //현재일자 기준으로 달력 화면호출
                break;
            case R.id.proEndCal:
                datePickerflag = FLAG_END_DATE;                                                                //달력 flag 종료일로 SET
                new DatePickerDialog(at, this, currentYear, currentMonth, currentDay).show();       //현재일자 기준으로 달력 화면호출
                break;
            case R.id.savBtn:
                saveContents();
                break;
            case R.id.cancelBtn:
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
        if ("".equals(String.valueOf(proStartTv.getText()))) {
            Toast.makeText(at, "프로젝트 시작일 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(String.valueOf(proEndTv.getText()))) {
            Toast.makeText(at, "프로젝트 종료일 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        int startDate = Integer.parseInt(String.valueOf(proStartTv.getText()).replaceAll("-", ""));
        int endDate = Integer.parseInt(String.valueOf(proEndTv.getText()).replaceAll("-", ""));
        if (startDate >= endDate) {
            Toast.makeText(at, "프로젝트 시작일과 종료일을 확인해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }


        //팀원 정보 입력값 체크
        String sTmp = "";
        if (iTeamCnt > 0) {
            for (int idx = 0; idx < iTeamStackCnt + 1; idx++) {
                Spinner sp = (Spinner) proDetailView.findViewById(nSP + idx);
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

    /**
     * 달력 선택 시 CallBack 함수
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String sTmpDateYear = String.valueOf(year);
        String sTmpDateMonth = String.valueOf(month + 1);
        String sTmpDateDay = String.valueOf(dayOfMonth);

        sTmpDateMonth = sTmpDateMonth.length() == 2 ? sTmpDateMonth : "0" + sTmpDateMonth;
        sTmpDateDay = sTmpDateDay.length() == 2 ? sTmpDateDay : "0" + sTmpDateDay;

        String sTmpYYYYMMDD = sTmpDateYear + "-" + sTmpDateMonth + "-" + sTmpDateDay;
        if (datePickerflag == FLAG_START_DATE) {
            proStartTv.setText(sTmpYYYYMMDD);
        } else if (datePickerflag == FLAG_END_DATE) {
            proEndTv.setText(sTmpYYYYMMDD);
        }
    }

}
