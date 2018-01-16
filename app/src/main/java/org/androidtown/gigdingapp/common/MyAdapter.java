package org.androidtown.gigdingapp.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.androidtown.gigdingapp.R;
import org.androidtown.gigdingapp.vo.ProRowDataViewHolder;
import org.androidtown.gigdingapp.vo.SchRowDataViewHolder;

import java.util.ArrayList;
import java.util.HashMap;


public class MyAdapter extends ArrayAdapter {

    private final int ITEM_VIEW_TYPE_NOTI = 0;
    private final int ITEM_VIEW_TYPE_PRO = 1;
    private final int ITEM_VIEW_TYPE_MAP = 2;
    private final int ITEM_VIEW_TYPE_SCH = 3;

    LayoutInflater lnf;

    ArrayList arrayDataLIst;

    int resourceLayout;
    int viewType;

    public MyAdapter(@NonNull Context context, int resource, ArrayList arrayLIst, int Type) {
        super(context, resource, arrayLIst);
        arrayDataLIst = arrayLIst;
        resourceLayout = resource;
        viewType = Type;
        lnf = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayDataLIst.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayDataLIst.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (viewType == ITEM_VIEW_TYPE_NOTI) {
            // 공지사항


        } else if (viewType == ITEM_VIEW_TYPE_PRO) {

            final ProRowDataViewHolder viewHolder;
            if (convertView == null) {
                convertView = lnf.inflate(resourceLayout, parent, false);
                viewHolder = new ProRowDataViewHolder();

                viewHolder.proNoTv = (TextView) convertView.findViewById(R.id.proNoTv);
                viewHolder.proNameTv = (TextView) convertView.findViewById(R.id.proNameTv);
                viewHolder.teamCntTv = (TextView) convertView.findViewById(R.id.teamCntTv);
                viewHolder.startDateTv = (TextView) convertView.findViewById(R.id.startDateTv);
                viewHolder.endDateTv = (TextView) convertView.findViewById(R.id.endDateTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ProRowDataViewHolder) convertView.getTag();
            }
            HashMap map = (HashMap) arrayDataLIst.get(position);
            viewHolder.proNoTv.setText(String.valueOf(map.get("project_code")));
            viewHolder.proNameTv.setText(String.valueOf(map.get("project_name")));
            viewHolder.teamCntTv.setText(String.valueOf(map.get("team_cnt")));
            viewHolder.startDateTv.setText(String.valueOf(map.get("start_ymd")));
            viewHolder.endDateTv.setText(String.valueOf(map.get("end_ymd")));

        } else if (viewType == ITEM_VIEW_TYPE_MAP) {
            // 지도


        } else if (viewType == ITEM_VIEW_TYPE_SCH) {
            HashMap map = (HashMap) arrayDataLIst.get(position);

            final SchRowDataViewHolder viewHolder;
            if (convertView == null) {
                convertView = lnf.inflate(resourceLayout, parent, false);
                viewHolder = new SchRowDataViewHolder();

                viewHolder.schDateTv = (TextView) convertView.findViewById(R.id.schDateTv);
                viewHolder.schTitleTv = (TextView) convertView.findViewById(R.id.schTitleTv);
                viewHolder.schPeriodTv = (TextView) convertView.findViewById(R.id.schPeriodTv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (SchRowDataViewHolder) convertView.getTag();
            }
            viewHolder.schDateTv.setText(String.valueOf(map.get(0)));
            viewHolder.schTitleTv.setText(String.valueOf(map.get(1)));
            viewHolder.schPeriodTv.setText(String.valueOf(map.get(2)));
        }
        return convertView;
    }

}
