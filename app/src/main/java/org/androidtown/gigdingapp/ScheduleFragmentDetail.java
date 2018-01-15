package org.androidtown.gigdingapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ScheduleFragmentDetail extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    FragmentActivity at;

    ViewGroup schDetailView;
    TextView schStartDateTv;
    TextView schEndtDateTv;
    TextView schStartTimeTv;
    TextView schEndtTimeTv;

    int currentYear = 0;        //현재기준 년도
    int currentMonth = 0;       //현재기준 월
    int currentDay = 0;         //현재기준 일
    int datePickerflag = 0;     //flag
    int timePickerflag = 0;     //flag
    int FLAG_START = 0;    //시작일 변수
    int FLAG_END = 1;      //종료일 변수


    public ScheduleFragmentDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        if (schDetailView == null) {
            schDetailView = (ViewGroup) inflater.inflate(R.layout.fragment_sch_detail, container, false);
            at = getActivity();
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

        AppCompatActivity AppCompat = (AppCompatActivity) getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_schedule_detail));
        AppCompat.findViewById(R.id.fab).setVisibility(View.INVISIBLE);

        schStartTimeTv = schDetailView.findViewById(R.id.schStartTimeTv);
        schEndtTimeTv = schDetailView.findViewById(R.id.schEndTimeTv);
        schStartDateTv = schDetailView.findViewById(R.id.schStartDateTv);
        schEndtDateTv = schDetailView.findViewById(R.id.schEndDateTv);

        schStartTimeTv.setOnClickListener(this);
        schEndtTimeTv.setOnClickListener(this);
        schStartDateTv.setOnClickListener(this);
        schEndtDateTv.setOnClickListener(this);
        schDetailView.findViewById(R.id.mapImgBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.schStartDateTv:
                datePickerflag = FLAG_START;    //달력 flag 시작일로 SET
                new DatePickerDialog(at, this, currentYear, currentMonth, currentDay).show();       //현재일자 기준으로 달력 화면호출
                break;
            case R.id.schEndDateTv:
                datePickerflag = FLAG_END;    //달력 flag 종료일로 SET
                new DatePickerDialog(at, this, currentYear, currentMonth, currentDay).show();       //현재일자 기준으로 달력 화면호출
                break;
            case R.id.schStartTimeTv:
                timePickerflag = FLAG_START;
                new TimePickerDialog(at, this, 15, 24, false).show();
                break;
            case R.id.schEndTimeTv:
                timePickerflag = FLAG_END;
                new TimePickerDialog(at, this, 17, 24, false).show();
                break;
            case R.id.savBtn:
                checkPermissionNSend();
                break;
            case R.id.cancelBtn:
                break;
            case R.id.mapImgBtn:
                break;
            default:
                break;

        }
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
        if (datePickerflag == FLAG_START) {
            schStartDateTv.setText(sTmpYYYYMMDD);
        } else if (datePickerflag == FLAG_END) {
            schEndtDateTv.setText(sTmpYYYYMMDD);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String sTmp = "";
        if (hourOfDay > 12) {
            sTmp = "오후 " + String.valueOf(hourOfDay - 12) + " : " + String.valueOf(minute);
        } else {
            sTmp = "오전 " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
        }

        if (timePickerflag == FLAG_START) {
            schStartTimeTv.setText(sTmp);
        } else if (timePickerflag == FLAG_END) {
            schEndtTimeTv.setText(sTmp);
        }
    }

    private void checkPermissionNSend () {
        if(ActivityCompat.checkSelfPermission(at, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(at, new String[]{Manifest.permission.INTERNET}, 1);
        } else {
            saveSchedule();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveSchedule();            // 권한 허가
                } else {
                    // 권한 거부 (사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다)
                    Toast.makeText(at, "인터넷 권한이 없어서 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 저장버튼 클릭 시
     */
    private void saveSchedule() {
        String sUrl = "http://www.calebslab.com/calebslab/caleb/android_php.php";
        String sStartDate = "";     // YYYYMMDDHHMM
        String sEndDate = "";       // YYYYMMDDHHMM
        String sTitle = "";
        String sContents = "";
        String sAderess = "";
        String sPushDsc = "0";  //0:미대상, 1:대상, 2:완료
        int iPushTime = 0;
        JSONObject json = null;
        RequestQueue request = Volley.newRequestQueue(at);

        // 데이타 세팅
        HashMap hashMap = new HashMap();
        hashMap.put("start_date", sStartDate);
        hashMap.put("end_date", sEndDate);
        hashMap.put("title", sTitle);
        hashMap.put("contents", sContents);

        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("code", "302");
            jsonData.put("input", hashMap);
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
            try {
                JSONObject jsonObject = response.getJSONObject("input");
                String sName = jsonObject.getString("name");
                JSONObject jsonObj = new JSONObject(jsonObject.getString("age"));
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

}
