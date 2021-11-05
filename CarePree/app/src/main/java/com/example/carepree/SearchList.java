/*

@ breif  : 도착지를 검색하는 페이지
@ detail : 도착지를 검색하는 페이지
@ why    : 도착지를 입력하기 위해 만들었다.

 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchList extends AppCompatActivity {

    EditText destination;

    String destinationAddress;
    String currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        // 현재위치를 받아온다
        getAddress();

    }

     /*
        함수명   : getAddress
        간략     : 현재 주소를 받아옵니다.
        상세     : 현재 주소를 받아옵니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        why      : 현재위치를 다시 다음 액티비티에 넘기기 위해 받아왔습니다.
     */

    public void getAddress(){
        Intent it = getIntent();
        currentAddress = it.getStringExtra("currentAddress");
    }

      /*
        함수명   : enterSearchResult
        간략     : SearchResult 페이지로 넘어갑니다.
        상세     : SearchResult 페이지로 정보랑 같이 넘깁니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : v : 클릭한 뷰
        why      : 도착지 정보와 현재 위치정보를 출발지와 도착지에 뿌리기 위해 같이 넘기기 위해 만들었습니다.
     */

    public void enterSearchResult(View v){ // 길찾기 화면으로 이동

        destination = (EditText)findViewById(R.id.destinationArea);
        destinationAddress = destination.getText().toString(); // 검색란에 적은 내용을 가져옴

        int tag =  Integer.parseInt(destination.getTag().toString()); // 태그로 어디에서 왔는지 구분하기 위해 사용

        Intent it = new Intent(this,SearchResult.class);
        it.putExtra("destinationAddress",destinationAddress); // 도착지 정보
        it.putExtra("currentAddress",currentAddress); // 현재위치 정보
        it.putExtra("it_tag",tag); // 1번태그

        startActivity(it);
    }

}