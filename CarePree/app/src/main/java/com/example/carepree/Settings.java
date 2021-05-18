package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    public void enterSearchList(View v){
        Intent it = new Intent(this,SearchList.class);
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