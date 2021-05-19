package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;


public class NearParkingLotMain extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    TMapView tMapView;
    String currentAddress;
    TMapData tmapdata;
    double currentLongitude;
    double currentLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_parking_lot_main);


        // GPS 사용 준비
        TMapGpsManager tMapGPS = new TMapGpsManager(this);

        // TMap GPS 설정
        tMapGPS.setMinTime(1); // 변경 인식시간
        tMapGPS.setMinDistance(10);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER); // 네트워크 방법
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER); // GPS방법 [위성기반] (작동 안 함)

        // GPS시작
        tMapGPS.OpenGps();



        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.map);

         // tMap 기본셋팅
        tMapView = new TMapView(this); // tMapView 객체 생성 (지도를 쓰기 위한)
        tMapView.setHttpsMode(true); // https 모드 허용해야 지도 뜸
        tMapView.setSKTMapApiKey("l7xxcffd8a912983400d81c75a095eb912e8"); // tMapView 기본셋팅 (앱키)

        // tMap 옵션추가
        tMapView.setSightVisible(true); // 어느 방향을 보고 있는지 보여줌
        tMapView.setCompassMode(true); // 자기가 바라보는 방향으로 돌아가는 나침판표시
        tMapView.setIconVisibility(true); //현재위치로 아이콘 표시 여부
        tMapView.setZoomLevel(17);


        linearLayoutTmap.addView(tMapView);

        new GettingCurrentPOI().execute();

    }

    public void enterSearchList(View v){ // 검색화면으로 이동
        Intent it = new Intent(this,SearchList.class);
        it.putExtra("currentAddress",currentAddress);
        Log.e("wef","current"+currentAddress);
        startActivity(it);
    }

    public void enterParkingLotList(View v){ // 추자장리스트로 이동
        Intent it = new Intent(this,ParkingLotList.class);
        startActivity(it);
    }

    public void enterFavorites(View v){ // 즐겨찾기로 이동
        Intent it = new Intent(this,Favorites.class);
        startActivity(it);
    }

    public void enterMain(View v){ // 메인으로 이동
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }

    public void enterSettings(View v){ // 설정으로 이동
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }

    @Override
    public void onLocationChange(Location location) {
        // 이걸 써야 처음 좌표와 다른위치로 이동했다는걸 인식해 그 좌표를 가져와서 거기로 이동(콜백 메소드)
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 서클을 현재 위치 중심좌표로 이동(좌표)
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude()); // 서클이 있는 곳으로 이동(현재위치로이동)
        TMapPoint tpoint = tMapView.getLocationPoint();
        currentLatitude = tpoint.getLatitude();
        currentLongitude = tpoint.getLongitude();
        tmapdata = new TMapData();


    }

    class GettingCurrentPOI extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                currentAddress = tmapdata.convertGpsToAddress(currentLatitude, currentLongitude);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}