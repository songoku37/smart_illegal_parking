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
import android.os.Handler;
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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(MainActivity.this, Login.class);
                startActivity(main);
                finish();
            }
        }, 3000);
    }



}