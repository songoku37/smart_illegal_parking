/*

@ breif  : 미완성
@ detail : 미완성
@ why    : 미완성


 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DepartureSearchList extends AppCompatActivity {

    EditText departureArea;
    String destinationAddress;
    String departureAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_search_list);

        getAddress();
    }

    public void getAddress(){
        Intent it = getIntent();
        departureAddress = it.getStringExtra("departureAddress"); // 출발지
        destinationAddress = it.getStringExtra("destinationAddress"); // 도착지
    }

    /*
        함수명   :
        간략     :
        상세     :
        작성자   :
        날짜     :
        return   :
        param    :
        why      :

     */

    public void enterFilteringFromDeparture(View v){ // 필터링 화면으로 이동
        Intent it = new Intent(this,Filtering.class);
        startActivity(it);
    }

    /*
        함수명   :
        간략     :
        상세     :
        작성자   :
        날짜     :
        return   :
        param    :
        why      :

     */

    public void enterSearchResultFromDeparture(View v){ // 길찾기 화면으로 이동

        departureArea = (EditText)findViewById(R.id.departureArea);
        departureAddress = departureArea.getText().toString(); // 도착지 검색란에 적은 내용

        int tag =  Integer.parseInt(departureArea.getTag().toString()); // 태그로 어디에서 왔는지 구분하기 위해 사용

        Intent it = new Intent(this,SearchResult.class);
        it.putExtra("it_tag",tag);
        it.putExtra("departureAddress",departureAddress); // 현재위치 정보
        it.putExtra("destinationAddress",destinationAddress); // 도착지 정보
        startActivity(it);
    }

    /*
        함수명   :
        간략     :
        상세     :
        작성자   :
        날짜     :
        return   :
        param    :
        why      :

     */


}