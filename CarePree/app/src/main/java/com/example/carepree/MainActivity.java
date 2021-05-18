package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void enterNearParkingLotMain(View v){ // 인근 주차장검색화면으로 이동
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }

}