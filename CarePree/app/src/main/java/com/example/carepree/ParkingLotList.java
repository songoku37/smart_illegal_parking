package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ParkingLotList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);
    }

    //- 끝에 걸쳐서 내리면 메인페이지 나오도록
    //- 체크 표시 누르면 바로 길찾기(주차장정보에 GPS값을 도착지)(출발지는 내 현재GPS)로 넘어가도록
    //- 주차장 정보 누르면 주차장정보페이지로 이동
    //- 현재위치로부터 가까운 거리순으로 정렬 현재GPS에서 주차장정보 GPS도착지 거리파악하는 메소드 이용하면 될듯
    //- 필터 적용하면 우선순위는 어떤 유형주차장 1번 2번 거리 or 돈

    public void enterParkingLotInformation(View v){ // 주차정보화면으로 이동
        Intent it = new Intent(this,ParkingLotInformation.class);
        startActivity(it);
    }

    public void enterFilteringFromParkingLotList(View v){ // 주차장 화면 리스트로 이동
        Intent it = new Intent(this,FilteringFromParkingLotList.class);
        startActivity(it);
    }

    public void enterSearchResult(View v){ // 길찾기 화면으로 이동
        Intent it = new Intent(this,SearchResult.class);
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