package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;

public class NearParkingLotMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_parking_lot_main);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.map);
        TMapView tMapView = new TMapView(this); // tMapView 객체 생성
        tMapView.setHttpsMode(true); // https 모드 허용해야 지도 뜸
        tMapView.setSKTMapApiKey("l7xxcffd8a912983400d81c75a095eb912e8"); // tMapView 기본셋팅 (앱키)

        tMapView.setSightVisible(true); // 어느 방향을 보고 있는지 보여줌
        tMapView.setCompassMode(true); // 자기가 바라보는 방향으로 돌아가는 나침판표시

        linearLayoutTmap.addView(tMapView);

        // 여기선 현재위치가 나오게 해야함
        // 검색에는 기본적인 내 위치가 나오도록 (현재위치 : ~~~)
        // 검색은 진짜 검색하는게 아니고 그거 버튼 누르면 SearchList로 넘어감
        // 슬라이드하면 근처주차장페이지 열리게하기

    }

    public void enterSearchList(View v){ // 검색화면으로 이동
        Intent it = new Intent(this,SearchList.class);
        startActivity(it);
    }

    public void enterParkingLotList(View v){ // 추자장리스트로 이동
        Intent it = new Intent(this,ParkingLotList.class);
        startActivity(it);
    }

    public void enterFavorites(View v){ // 즐겨찾기로 이동
        Intent it = new Intent(this,Favorites.class);
        startActivity(it);
    }

    public void enterMain(View v){ // 메인으로 이동
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }

    public void enterSettings(View v){ // 설정으로 이동
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }

}