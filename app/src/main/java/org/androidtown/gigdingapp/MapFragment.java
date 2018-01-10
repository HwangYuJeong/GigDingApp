package org.androidtown.gigdingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapFragment extends Fragment {

    public MapFragment() {
        // Required empty public constructor
    }

    private MapView mapView;
    private MapPoint[] mapPoint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Context context = getContext();
        mapView = new MapView(context);
        mapPoint = new MapPoint[] {MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633),
                                     MapPoint.mapPointWithGeoCoord(37.5002, 127.0358)};

        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        for(int i = 0; i < 2; i++) {
            setMarker(mapView, mapPoint[i], i);
        }
        mapViewContainer.addView(mapView);

        return view;
    }

    public void getMarkerInfo() {
        String name = "TEST_거래처이름";
        String tel = "010-0000-1111";
        String damdang = "당다라당당당당당";
    }

    public void setMarker(MapView mapView, MapPoint mapPoint, int index) {
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("회사");
        marker.setTag(index);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
    }

    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.map_marker, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            String name = "TEST_거래처이름";
            String tel = "010-0000-1111";
            String damdang = "당다라당당당당당";
            ((TextView) mCalloutBalloon.findViewById(R.id.map_marker_title)).setText(name);
            ((TextView) mCalloutBalloon.findViewById(R.id.map_marker_name)).setText(damdang);
            ((TextView) mCalloutBalloon.findViewById(R.id.map_marker_telNo)).setText(tel);
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }
}
