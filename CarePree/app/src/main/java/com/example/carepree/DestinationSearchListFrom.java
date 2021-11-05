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

public class DestinationSearchListFrom extends AppCompatActivity {

    EditText destination;

    String destinationAddress;
    String departureAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_search_list);

        getAddress();
    }

    public void getAddress(){
        Intent it = getIntent();
        departureAddress = it.getStringExtra("departureAddress");
        destinationAddress = it.getStringExtra("destinationAddress");
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

    public void enterSearchResultFromArrival(View view){ // 길찾기 화면으로 이동


        destination = (EditText)findViewById(R.id.destinationArea);
        destinationAddress = destination.getText().toString(); // 도착지 검색란에 값을 가져옴

        int tag =  Integer.parseInt(destination.getTag().toString()); // 태그로 어디에서 왔는지 구분하기 위해 사용

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