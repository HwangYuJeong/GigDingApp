package org.androidtown.gigdingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.androidtown.gigdingapp.common.ComUtil;
import org.androidtown.gigdingapp.common.MyAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

public class ProFragment extends Fragment implements ListView.OnItemClickListener {

    ArrayList arrayProjectList = new ArrayList();
    ViewGroup proView;
    MyAdapter adapter;
    ListView listV;

    public ProFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pro, container, false);

        if (proView == null) {
            proView = (ViewGroup) inflater.inflate(R.layout.fragment_pro, container, false);
            adapter = new MyAdapter(getActivity(), R.layout.project_list_row, arrayProjectList, 1);
            listV = (ListView) proView.findViewById(R.id.ProListView);
            listV.setOnItemClickListener(this);
        } else {
            ViewGroup parentViewGroup = (ViewGroup) proView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(proView);
            }
        }
        return proView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 타이틀 SET
        AppCompatActivity AppCompat = (AppCompatActivity) getActivity();
        AppCompat.getSupportActionBar().setTitle(getString(R.string.nav_project));
        AppCompat.findViewById(R.id.fab).setVisibility(View.VISIBLE);

        serchList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap hashMap = new HashMap((HashMap) adapter.getItem(position));

        Bundle args = new Bundle();
        args.putString("project_code", (String) hashMap.get("project_code"));
        args.putString("project_name", (String) hashMap.get("project_name"));
        args.putInt("team_cnt", (Integer) hashMap.get("team_cnt"));
        args.putString("start_ymd", (String) hashMap.get("start_ymd"));
        args.putString("end_ymd", (String) hashMap.get("end_ymd"));

        Fragment fragemnt = new ProFragmentDetail();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragemnt.setArguments(args);
        ft.replace(R.id.content_fragment_layout, fragemnt).addToBackStack(null).commit();
    }

    /**
     * 조회
     */
    private void serchList() {
        ComUtil comUT = new ComUtil();

        arrayProjectList.clear();
        String sUrl = getString(R.string.send_page_url_1);
        String sCode = "201";
        String sNowDate = comUT.com_toDate();
        sNowDate = sNowDate.substring(0,4) + "-" + sNowDate.substring(4,6);
        Log.d("JHPARK", "sNowDate   = " + sNowDate);
        sNowDate = "2016-04";

        JSONObject json = null;
        RequestQueue request = Volley.newRequestQueue(getActivity());

        // 데이타 세팅
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("code", sCode);
            jsonData.put("now_date", sNowDate);
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
                JSONArray jsonArray = new JSONArray(jsonObject.getString("result"));

                String project_code = "";
                String project_name = "";
                String project_detail = "";
                String start_ymd = "";
                String end_ymd = "";
                String com_code = "";
                String com_name = "";
                int team_cnt=0;

                for(int idx=0; idx<jsonArray.length(); idx++) {
                    JSONObject subJsonObject = new JSONObject(jsonArray.getString(idx));
                    HashMap map = new HashMap();

                    project_code = "";
                    project_name = "";
                    project_detail = "";
                    start_ymd = "";
                    end_ymd = "";
                    com_code = "";
                    com_name = "";
                    team_cnt = 0;

                    project_code = subJsonObject.getString("project_code");
                    project_name = subJsonObject.getString("project_name");
                    project_detail = subJsonObject.getString("project_detail");
                    start_ymd = subJsonObject.getString("start_ymd");
                    end_ymd = subJsonObject.getString("end_ymd");
                    com_code = subJsonObject.getString("com_code");
                    com_name = subJsonObject.getString("com_name");
                    team_cnt = subJsonObject.getInt("team_cnt");


                    map.put("project_code", project_code);
                    map.put("project_name", project_name);
                    map.put("project_detail", project_detail);
                    map.put("start_ymd", start_ymd);
                    map.put("end_ymd", end_ymd);
                    map.put("com_code", com_code);
                    map.put("com_name", com_name);
                    map.put("team_cnt", team_cnt);

                    Log.d("JHPARK", "project_code " + project_code + "   project_name " + project_name+ "   project_detail " + project_detail+
                            "   start_ymd " + start_ymd+ "   end_ymd " + end_ymd+ "   com_code " + com_code+ "   com_name " + com_name + "    team_cnt = " + team_cnt);

                    arrayProjectList.add(map);
                }
                listV.setAdapter(adapter);

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
