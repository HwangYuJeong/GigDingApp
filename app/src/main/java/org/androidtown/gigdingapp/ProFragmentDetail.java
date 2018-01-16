package org.androidtown.gigdingapp;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.androidtown.gigdingapp.common.ComUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class ProFragmentDetail extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    // 동적 레이아웃 생성 시 필요한 변수
    int nTR = 1000; //TableLy
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
    LinearLayout teamLinearLy;
    EditText proNameEv;     //프로젝트명 EditView
    EditText proDetailEv;   //프로젝트 상세 EditView
    TextView proStartTv;    //프로젝트 시작일 View
    TextView proEndTv;      //프로젝트 종료일 View


    public ProFragmentDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        if(proDetailView == null) {
            proDetailView = (ViewGroup) inflater.inflate(R.layout.fragment_pro_detail, container, false);

            at = getActivity();
            arrayTmp = getResources().getStringArray(R.array.team);
            teamLinearLy = (LinearLayout) proDetailView.findViewById(R.id.teamLinearLy);
            adapter = new ArrayAdapter<String>(at, android.R.layout.simple_spinner_item, arrayTmp);

            proDetailView.findViewById(R.id.teamAddBtn).setOnClickListener(this);
            proDetailView.findViewById(R.id.savBtn).setOnClickListener(this);
            proDetailView.findViewById(R.id.proStartCal).setOnClickListener(this);
            proDetailView.findViewById(R.id.proEndCal).setOnClickListener(this);

            proStartTv = proDetailView.findViewById(R.id.proStartTv);
            proEndTv = proDetailView.findViewById(R.id.proEndTv);
            proNameEv = proDetailView.findViewById(R.id.proNameEv);
            proDetailEv = proDetailView.findViewById(R.id.proNameDetailEv);

        } else {
            ViewGroup parentViewGroup = (ViewGroup) proDetailView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(proDetailView);
            }
        }

        return proDetailView;
    }

    /**
     * 조회
     */
    private void serchList(String project_code) {

        String sUrl = getString(R.string.send_page_url_1);
        String sCode = "202";

        JSONObject json = null;
        RequestQueue request = Volley.newRequestQueue(getActivity());

        // 데이타 세팅
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("code", sCode);
            jsonData.put("project_code", project_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, sUrl, jsonData, successCallback, failCallback);
        request.add(jsonRequest);

        /** Volley 에서 사용하는 옵션 **/
        int socketTimeout = 20000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonRequest.setRetryPolicy(policy);
    }

    Response.Listener<JSONObject> successCallback = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.d("JHPARK", "onResponse: " + response.toString());

            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener failCallback = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            volleyError.printStackTrace();
            Log.d("JHPARK", "오류!!");
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity AppCompat = (AppCompatActivity)getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_project_detail));
        AppCompat.findViewById(R.id.fab).setVisibility(View.INVISIBLE);

        init();
    }

    private void init() {

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);
        currentDay = cal.get(Calendar.DAY_OF_MONTH);

        Bundle args = getArguments();
        if (args != null) {
            Toast.makeText(at, "proNo = " + args.getString("proNo"), Toast.LENGTH_SHORT).show();
            String project_code = args.getString("proNo");
            serchList(project_code);
        }

    }

    /**
     * 팀원 정보 동적 레이아웃 생성
     */
    private void addView() {

        int width250 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        int width80 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());

        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(width250, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(width80, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        lp1.setMargins(50, 0, 50, 0);
        lp2.setMargins(50, 0, 50, 0);

        TableLayout teamLy = new TableLayout(at);
        TableRow tableRow1 = new TableRow(at);
        TextView textView1 = new TextView(at);
        Spinner sp = new Spinner(at);
        Button bt = new Button(at);

        teamLy.setId(nTR + iTeamStackCnt);

        textView1.setLayoutParams(lp1);
        textView1.setText("팀원");
        textView1.setGravity(Gravity.LEFT);
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);

        sp.setAdapter(adapter);
        sp.setId(nSP + iTeamStackCnt);

        bt.setText("삭제");
        bt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        bt.setLayoutParams(lp2);
        bt.setId(nBT + iTeamStackCnt);

        tableRow1.addView(bt);
        tableRow1.addView(textView1);
        tableRow1.addView(sp);
        teamLy.addView(tableRow1);

        TableRow tableRow2 = new TableRow(at);
        TextView textView2_1 = new TextView(at);
        TextView textView2_2 = new TextView(at);

        textView2_1.setLayoutParams(lp1);
        textView2_2.setLayoutParams(lp1);
        textView2_1.setHint("투입일");
        textView2_2.setHint("철수일");
        textView2_1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        textView2_2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        textView2_1.setBackgroundColor(Color.parseColor("#e1dedd"));
        textView2_2.setBackgroundColor(Color.parseColor("#e1dedd"));

        tableRow2.addView(textView2_1);
        tableRow2.addView(textView2_2);
        teamLy.addView(tableRow2);

        TableRow tableRow3 = new TableRow(at);
        EditText editText = new EditText(at);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);

        editText.setLayoutParams(lp1);
        editText.setHint("기술내역");
        tableRow3.addView(editText);
        teamLy.addView(tableRow3);

        //teamLy.setTouch

        teamLinearLy.addView(teamLy);



        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iCnt = Integer.parseInt(String.valueOf(v.getId()).substring(1));
                TableLayout tbR = (TableLayout) proDetailView.findViewById(nTR + iCnt);
                tbR.removeAllViews();
                iTeamCnt = iTeamCnt - 1;
            }
        });

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
