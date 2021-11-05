/*

@ breif  : 이 어플의 4가지 핵심 기능을 선택할 수 있는 페이지
@ detail : 인근주차장검색 , 불법주정차구역확인 , 불법주정차신고기능 , 주차구역 거래소
@ why    : 불법주정차를 소프트웨어적으로 해결하는 기능을 소개하고 선택하기 위해 만들었다

 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void enterTmapNavi(View v){ // 출발지 검색화면으로 이동
        Intent it = new Intent(this, TmapNavi.class);
        startActivity(it);
    }
     /*
        함수명   : enterNearParkingLotMain
        간략     : 인근주차장 메인페이지로 넘어가는 onClick
        상세     : 인근주차장 알려주는 페이지로 넘어가는 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : v : 클릭한 View객체
        why      : 해당 페이지로 넘어가야 기능을 제공해줄 수 있기 때문에
     */


    public void enterNearParkingLotMain(View v){ // 인근 주차장검색화면으로 이동
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }




}