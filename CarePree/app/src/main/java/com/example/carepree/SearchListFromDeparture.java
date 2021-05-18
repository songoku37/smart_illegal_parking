package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchListFromDeparture extends AppCompatActivity {

    EditText departureArea;
    String departureAreaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list_from_departure);
    }

    public void enterFilteringFromDeparture(View v){ // 필터링 화면으로 이동
        Intent it = new Intent(this,Filtering.class);
        startActivity(it);
    }
    public void enterSearchResultFromDeparture(View v){ // 길찾기 화면으로 이동

        departureArea = (EditText)findViewById(R.id.departureArea);
        departureAreaText = departureArea.getText().toString();
        String tag = (String) departureArea.getTag(); // 태그로 어디에서 왔는지 구분하기 위해 사용
        Intent it = new Intent(this,SearchResult.class);
        it.putExtra("it_tag",tag);
        it.putExtra("departureAreaText",departureAreaText);
        startActivity(it);
    }
}