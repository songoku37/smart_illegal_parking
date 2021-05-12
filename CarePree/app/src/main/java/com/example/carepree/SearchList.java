package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SearchList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        // 검색란엔 현재위치가 디폴트값
        // 최근검색이 밑으로 쭉 나오도록
        // 길찾기 누르면 그 GPS값이나 이름을 SearchResult에 넘긴다
        // 그리고 주차장이라고 치면 무조건 가까운 순으로 정렬 가격은 옆에 살짝 적어두기
        //- 필터 적용하면 우선순위는 어떤 유형주차장 + 운영요일 1번 2번은 거리 or 돈
        // 즐겨찾기 누르면 그 정보가 즐겨찾기에 추가
    }

    public void enterFiltering(View v){
        Intent it = new Intent(this,Filtering.class);
        startActivity(it);
    }

    public void enterSearchResult(View v){
        Intent it = new Intent(this,SearchResult.class);
        startActivity(it);
    }

}