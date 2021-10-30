/*

@ breif  : 인근 주차장 옵션을 선택해서 들어오면 보이는 첫번째 화면
@ detail : 인근 주차장 목록을 보여주고 자신의 현재위치를 보여준다 또한 검색할 수 있는 액티비티로 넘어갈 수 있다.
@ why    : 자신의 현재위치를 보여줘서 어디있는지에 대한 흥미를 유발하고 현재위치에 정보를 다른 액티비티에서 활용하기 위해 만들었다.
           그리고 인근주차장 옵션이기 때문에 그에 대한 걸 바로 메인에 보여주기 위함도 있다.

 */
package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;


public class NearParkingLotMain extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    TMapView tMapView;
    TMapData tmapdata;
    TMapGpsManager tMapGPS;

    String currentAddress;

    double currentLongitude;
    double currentLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_parking_lot_main);

        new GettingCurrentPOI().execute();

        createMaps();

    }



    public void createMaps(){
        // GPS 사용 준비
        tMapGPS = new TMapGpsManager(this);
        // TMap GPS 설정
        tMapGPS.setMinTime(1); // 변경 인식시간
        tMapGPS.setMinDistance(10);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER); // 네트워크 방법
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER); // GPS방법 [위성기반] (작동 안 함)

        // GPS시작
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
        }

        tMapGPS.OpenGps();

        // tMap 기본셋팅
        tMapView = new TMapView(this); // tMapView 객체 생성 (지도를 쓰기 위한)
        tMapView.setHttpsMode(true); // https 모드 허용해야 지도 뜸
        tMapView.setSKTMapApiKey("l7xxcffd8a912983400d81c75a095eb912e8"); // tMapView 기본셋팅 (앱키)

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.map); // 레이아웃에 있는 Id값 받아와서 여기에다 지도 표시하기 위한 준비

        // tMap 옵션추가
        tMapView.setSightVisible(true); // 어느 방향을 보고 있는지 보여줌
        tMapView.setCompassMode(true); // 자기가 바라보는 방향으로 돌아가는 나침판표시
        tMapView.setIconVisibility(true); //현재위치로 아이콘 표시 여부
        tMapView.setZoomLevel(17);


        linearLayoutTmap.addView(tMapView); //
    }



     /*
        함수명   : enterSearchList
        간략     : 검색페이지로 이동합니다.
        상세     : 클릭시 도착지를 검색하는 페이지로 이동하는 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : v : 클릭한 View객체
        why      : 검색페이지로 넘어가기 위해 만들었습니다.
     */

    public void enterSearchList(View v){ // 검색화면으로 이동

        if(currentAddress == null){
            Toast.makeText(this,"현재 위치정보를 받아오고 있습니다." ,Toast.LENGTH_LONG).show();
            new GettingCurrentPOI().execute();

        }else{
            Intent it = new Intent(this,SearchList.class);
            it.putExtra("currentAddress",currentAddress);
            startActivity(it);
        }


    }

     /*
        함수명   : enterParkingLotList
        간략     : 인근 주차장 목록으로 넘어갑니다.
        상세     : 2km내에 주차장 목록이 있는 페이지로 넘어가는 onCLick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : v : 클릭한 View객체
        why      : 인근 주차장 목록 페이지로 넘어가기 위해 만들었습니다.
     */

    public void enterParkingLotList(View v){ // 주차장리스트로 이동
        if(currentAddress == null){
            new GettingCurrentPOI().execute();
            Toast.makeText(this,"현재 위치정보를 받아오고 있습니다." ,Toast.LENGTH_LONG).show();
        }else{
            Intent it = new Intent(this,ParkingLotList.class);
            it.putExtra("currentAddress",currentAddress); // 현재 주소
            it.putExtra("currentLatitude",currentLatitude); // 현재 위도
            it.putExtra("currentLongitude",currentLongitude); // 현재 경도
            startActivity(it);
        }
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
        함수명   : onLocationChange
        간략     : 위치가 바뀌면 콜백되는 메소드
        상세     : 현재위치를 나타내고 이동하는 서클을 보여주는 메소드
        작성자   : 신희빈
        날짜     : 2021.06.05
        param    : location : 현재 위치
        why      : 위치가 바뀔때마다 현재 위치를 나타내는 서클을 갱신해줄 필요가 있기 때문에 만들었다.
     */

    @Override
    public void onLocationChange(Location location) {
        // 이걸 써야 처음 좌표와 다른위치로 이동했다는걸 인식해 그 좌표를 가져와서 거기로 이동(콜백 메소드)
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 서클을 현재 위치 중심좌표로 이동(좌표)
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude()); // 서클이 있는 곳으로 이동(현재위치로이동)
        TMapPoint tpoint = tMapView.getLocationPoint();
        currentLatitude = tpoint.getLatitude();
        currentLongitude = tpoint.getLongitude();
        tmapdata = new TMapData();

        new GettingCurrentPOI().execute();
    }


         /*
           클래스명 : GettingCurrentPoI
           간략     : 현재 위치 주소를 받아오는 작업은 무조건 비동기식으로 처리해야한다.
           상세     : 현재 위치 주소를 받아오는 작업은 무조건 비동기식으로 처리해야한다.
           작성자   : 이성재
           날짜     : 2021.06.05
         */


    class GettingCurrentPOI extends AsyncTask<Void,Void,Void>{

        /*
           함수명   : doInBackground
           간략     : 현재 위치 주소를 받아온다.
           상세     : 현재 위치 주소를 받아온다.
           작성자   : 이성재
           날짜     : 2021.06.05
           why      : 현재 위치가 필요한 액티비티에 null값으로 넘어가면 안 되기 때문에 강제로 이 함수를 이용해 받아오게하기 위해서 만들었습니다.
        */

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                currentAddress = tmapdata.convertGpsToAddress(currentLatitude, currentLongitude);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (NullPointerException e){

            }
            return null;
        }


    }
}

