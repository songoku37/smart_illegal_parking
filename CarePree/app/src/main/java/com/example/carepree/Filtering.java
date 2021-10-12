/*

@ breif  : 미구현
@ detail : 미구현
@ why    : 주차장에 옵션들이 되게 많기 때문에 자기가 원하는 주차장을 필터링해서 쉽게 찾기위해서 만들었다.

 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Filtering extends AppCompatActivity {

    // private var auth : FirebaseAuth? = null      사용할 액티비티에서 전역변수로 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);

        // auth = Firebase.auth;               onCreate 에서 할당

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



    public void enterSearchList(View v){ // 검색화면으로 이동
        Intent it = new Intent(this,SearchList.class);
        startActivity(it);
    }

     /*
        함수명   : enterFavorites
        간략     : 즐겨찾기 페이지로 이동
        상세     : 즐겨찾기로 페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : v : 클릭한 View객체
        why      : 즐겨찾기 페이지로 이동하기 위해 만들었습니다.
     */

    public void enterFavorites(View v){ // 즐겨찾기로 이동
        Intent it = new Intent(this,Favorites.class);
        startActivity(it);
    }

     /*
        함수명   : enterMain
        간략     : 메인페이지로 이동
        상세     : 메인페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : v : 클릭한 View객체
        why      : 메인페이지로 이동하기 위해 만들었습니다.
     */

    public void enterMain(View v){ // 메인으로 이동
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }

     /*
        함수명   : enterSettings
        간략     : 설정 페이지로 이동
        상세     : 설정페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : v : 클릭한 View객체
        why      : 설정 페이지로 이동하기 위해 만들었습니다.
     */

    public void enterSettings(View v){ // 설정으로 이동
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }



}