package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {
    // 발급받은 키를 API_KEY에 입력
    String API_Key = "l7xxcffd8a912983400d81c75a095eb9" +
            "12e8";

    // T Map View 기본값 설정
    TMapView tMapView = null;

    // T Map GPS 기본값 설정
    TMapGpsManager tMapGPS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // T Map View
        tMapView = new TMapView(this);

        // API Key 값 불러오기
        tMapView.setSKTMapApiKey(API_Key);

        // TMap설정
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);                   //현재위치로 아이콘 표시 여부
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);     // 지도의 종류를 일반지도로 보여줌
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);     // 한국어로 지원
        tMapView.setSightVisible(true);                     // 바라보는방향 표시
        tMapView.setTrackingMode(true);                     // 현재 위치로 지도 이동
        tMapView.setCompassMode(true);                      // 나침반 사용 설정
        tMapView.setPOIRotate(true);                        // 나침반 회전시 POI 이미지를 같이 회전시킬지 여부 설정
        tMapView.setMarkerRotate(true);                     // 나침반 회전시 마커 이미지를 같이 회전시킬지 여부 설정
        tMapView.setTrafficInfoActive(true);                // 교통정보 표출여부 설정

        // 임의로 넣은 현재 좌표값 -> 핸드폰 gps 연결시 이거만 없애면 됨
        tMapView.setLocationPoint(126.65665468472191, 37.60196573254516);

        /*
        //화면중심 좌표의 위도,경도 반환
        TMapPoint tpoint = tMapView.getLocationPoint();
        double Latitude = tpoint.getLatitude();
        double Longitude = tpoint.getLongitude();
        */

        // T Map View Linear Layout 사용
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);

        // GPS 퍼미션 사용
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // GPS를 사용하여 T Map 불러오기
        tMapGPS = new TMapGpsManager(this);

        // TMap GPS 설정
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER); // 네트워크 방법
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER); // GPS방법

        tMapGPS.OpenGps();

    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

}

