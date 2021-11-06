package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PostPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpage);
    }

     /*
        함수명   : enterEditingPost
        간략     : 게시판 수정페이지로 이동
        상세     : 게시판 수정페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.11.06
        param    : View (클릭한 View객체)
        why      : 게시판 수정페이지로 이동하기 위해 만들었습니다.
     */

    public void enterEditingPost(View v){ // 설정으로 이동
        Intent it = new Intent(this,PostEditing.class);
        startActivity(it);
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
}