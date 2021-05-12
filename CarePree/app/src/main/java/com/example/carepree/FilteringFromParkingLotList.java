package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FilteringFromParkingLotList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering_from_parking_lot_list);
    }

    public void enterParkingLotList(View v){
        Intent it = new Intent(this,ParkingLotList.class);
        startActivity(it);
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