package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Board extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
    }

     /*
        함수명   : enterBoard
        간략     : 게시판으로 이동
        상세     : 게시판으로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.11.06
        param    : View (클릭한 View객체)
        why      : 게시판 페이지로 이동하기 위해 만들었습니다.
     */


    public void enterBoard(View v){ // 메인으로 이동
        Intent it = new Intent(this,Board.class);
        startActivity(it);
    }

     /*
        함수명   : enterMain
        간략     : 메인페이지로 이동
        상세     : 메인페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : View (클릭한 View객체)
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
     /*
        함수명   : enterWritingPost
        간략     : 글쓰기로 이동
        상세     : 글쓰기로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.11.06
        param    : View (클릭한 View객체)
        why      : 게시판 글쓰기 페이지로 이동하기 위해 만들었습니다.
     */

    public void enterWritingPost(View v){ // 설정으로 이동
        Intent it = new Intent(this,PostWriting.class);
        startActivity(it);
    }

     /*
        함수명   : enterPostPage
        간략     : 게시판 수정페이지로 이동
        상세     : 게시판 수정페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.11.06
        param    : View (클릭한 View객체)
        why      : 게시판 수정페이지로 이동하기 위해 만들었습니다.
     */

    public void enterPostPage(View v){ // 설정으로 이동
        Intent it = new Intent(this,PostPage.class);
        startActivity(it);
    }
}