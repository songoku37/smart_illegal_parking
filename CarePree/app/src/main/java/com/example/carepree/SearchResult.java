package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import static android.graphics.BitmapFactory.decodeResource;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.search_result_map);
        TMapView tMapView = new TMapView(this); // tMapView 객체 생성
        tMapView.setHttpsMode(true); // https 모드 허용해야 지도 뜸
        tMapView.setSKTMapApiKey("l7xxcffd8a912983400d81c75a095eb912e8"); // tMapView 기본셋팅 (앱키)

        tMapView.setSightVisible(true); // 어느 방향을 보고 있는지 보여줌
        tMapView.setCompassMode(true); // 자기가 바라보는 방향으로 돌아가는 나침판표시

        new TmapAsyncTask().execute(tMapView);

        linearLayoutTmap.addView(tMapView);

    }

    public void enterFavorites(View v){
        Intent it = new Intent(this,Favorites.class);
        startActivity(it);
    }

    public void enterMain(View v){
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }

    public void enterSettings(View v){
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }
    class TmapAsyncTask extends AsyncTask<TMapView,Void,TMapView> {


        @Override
        protected void onPreExecute(){
            Log.e("__LOG__","onPostExecute");
            super.onPreExecute();
        }

        @Override
        protected TMapView doInBackground(TMapView ... TmapViews) {
            TMapPoint tMapPointStart = new TMapPoint(37.570841, 126.985302); // 출발지
            TMapPoint tMapPointEnd = new TMapPoint(37.551135, 126.988205); // N서울타워(목적지)
            try {
                TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                // 경로를 만드는 tMapPolyLine 객체 생성과 출발지와 도착지 설정
                tMapPolyLine.setLineColor(Color.BLUE); // 경로 색상
                tMapPolyLine.setLineWidth(2); // 경로 선의 두께
                TmapViews[0].addTMapPolyLine("Line1", tMapPolyLine); // 경로를 그린다
                TmapViews[0].addTMapPath(tMapPolyLine); // 이거 안 쓰면 밑에 아이콘 사용 불가

                Bitmap start = decodeResource(getResources(),R.drawable.start); // 출발지 아이콘
                Bitmap end = decodeResource(getResources(),R.drawable.end); // 도착지 아이콘
                TmapViews[0].setTMapPathIcon(start, end); // 출발 도착 아이콘 설정

            }catch(Exception e) {
                e.printStackTrace();
            }
            return TmapViews[0];
        }


        @Override
        protected void onPostExecute(TMapView TmapViews) {
            super.onPostExecute(TmapViews);
            // super.onPostExecute는 doInBackground에서 값이 안 넘어와도 무조건 써야함 void값으로 매개변수 넣어도 작동함
            Log.e("__LOG__","onPostExecute");
        }

    }
}

