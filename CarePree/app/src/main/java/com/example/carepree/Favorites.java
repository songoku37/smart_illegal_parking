/*

@ breif  : 주차장의 즐겨찾기 페이지
@ detail : 자주 애용하는 주차장을 즐겨찾기로 등록했을 때 그 주차장의 목록이 여기에 들어가게 된다.
@ why    : 자주 가는 주차장을 매번 찾기 힘들기 때문에 즐겨찾기 페이지가 있으면 바로 검색 없이 길찾기가 가능하기 때문에 만들었다.

 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        // 주차장 목록들 위치 변경 가능하도록
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


    public void enterSearchList(View v){
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