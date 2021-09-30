package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.skt.Tmap.TMapTapi;

import java.util.ArrayList;
import java.util.HashMap;

public class TmapNavi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap_navi);

        Intent it = getIntent();


        String departureAddress = it.getStringExtra("departureAddress"); // 현재위치명
        double tempLatitude = it.getDoubleExtra("latitude",37.5283169); // 현재위치 위도
        double tempLongitude = it.getDoubleExtra("longitude",126.9294254); // 현재위치 경도

        String destinationAddress = it.getStringExtra("destinationAddress"); // 도착지명
        double tempLatitude2 = it.getDoubleExtra("latitude2",37.566385f); // 도착지 위도
        double tempLongitude2 = it.getDoubleExtra("longitude2",126.984098f); // 도착지 경도

        String latitude = String.valueOf(tempLatitude);
        String longitude = String.valueOf(tempLongitude);

        String latitude2 = String.valueOf(tempLatitude2);
        String longitude2 = String.valueOf(tempLongitude2);


        TMapTapi tmaptapi = new TMapTapi(this);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> pathInfo = new HashMap<String, String>();

        // 도착지
        pathInfo.put("rGoName", destinationAddress);
        pathInfo.put("rGoY", latitude2);
        pathInfo.put("rGoX", longitude2);

        // 출발지
        pathInfo.put("rStName", departureAddress);
        pathInfo.put("rStY", latitude);
        pathInfo.put("rStX", longitude);

        boolean isTmapApp = tmaptapi.isTmapApplicationInstalled();

        if(isTmapApp){
            tmaptapi.invokeRoute(pathInfo);
        }else{
            Uri uri = Uri.parse(tmaptapi.getTMapDownUrl().get(0));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }
}