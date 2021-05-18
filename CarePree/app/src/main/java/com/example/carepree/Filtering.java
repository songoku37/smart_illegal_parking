package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Filtering extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);

    }

    public void enterSearchList(View v){ // 검색화면으로 이동
        Intent it = new Intent(this,SearchList.class);
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