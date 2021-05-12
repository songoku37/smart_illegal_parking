package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParkingLotInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_information);

        // 주차장정보페이지	- 공공데이터에서 파싱한 거 뿌리기
        // 공유 버튼 누르면 공유페이지뜨게하기 (정보공유페이지      - 누르면 그 어플 열려서 공유할 수 있게 하기)
    }

    public void enterFavorites(View v){
        Intent it = new Intent(this,Favorites.class);
        startActivity(it);
    }

    public void enterMain(View v){
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }

    public void enterSettings(View v){
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }
}