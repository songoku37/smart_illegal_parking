package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchListFromArrival extends AppCompatActivity {

    EditText destination;
    String destinationTextFromArrival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list_from_arrival);
    }

    public void enterFilteringFromArrival(View view){ // 필터링 화면으로 이동
        Intent it = new Intent(this,Filtering.class);
        startActivity(it);
    }
    public void enterSearchResultFromArrival(View view){ // 길찾기 화면으로 이동

        destination = (EditText)findViewById(R.id.destination);
        destinationTextFromArrival = destination.getText().toString();
        Intent it = new Intent(this,SearchResult.class);
        String tag = (String) destination.getTag(); // 태그로 어디에서 왔는지 구분하기 위해 사용
        it.putExtra("it_tag",tag);
        it.putExtra("destinationTextFromArrival",destinationTextFromArrival);
        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "여기에선 뒤로가기가 막혀있습니다.", Toast.LENGTH_SHORT).show();

    }

}