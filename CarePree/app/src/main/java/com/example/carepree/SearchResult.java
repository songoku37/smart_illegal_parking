/*

@ breif  : 도착지 인근에 불법주정차구역과 길찾기기능이 있는 페이지
@ detail : 출발지와 도착지로 길찾기 표시를 해주고 얼마나 걸리는지와 도착지에 불법주정차구역이 있는지 알려주기 위해 만들었다.
@ why    : 출발지와 도착지를 기준으로 길찾기 기능과 도착지에 인근에 불법주정차구역이 어디있는지 알려주기위해 만들었다.


 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapPolygon;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import static android.graphics.BitmapFactory.decodeResource;

public class SearchResult extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    // json 파일 이름
    String bridgeJsonNm = "bridge_json";
    String crosswalkJsonNm = "crosswalk_json";
    String schoolZoneJsonNm = "school_zone_json";
    String onlyPedestrianJsonNm = "only_pedestrian_json";
    String pedestrianPriorityJsonNm = "pedestrian_priority_json";
    String fireTruckJsonNm = "fire_truck_json";
    String firefightingWaterFacility = "firefighting_water_facility_json";


    final double PER_LATITUDE = 0.0090909090909091; // 1km당 위도
    final double PER_LONGITUDE = 0.0112688753662384; // 1km당 경도

    TMapPoint tMapPointStart;
    TMapPoint tMapPointEnd;
    TMapView tMapView;

    Intent it;

    int tag;

    double latitude=0;
    double longitude=0;
    double latitude2=0;
    double longitude2=0;

    String destinationPOIName;
    String departurePOIName;
    String destinationAddress;
    String departureAddress;

    Context thisContext = this;

    boolean locationButton = true;
    boolean checkingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        // 태그값 확인하고 값 받아오기
        checkTagNumber();
        createMap();

        new POIPointAsynTask().execute(departureAddress, destinationAddress);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(latitude != 0 && longitude !=0){
            if(latitude2 !=0 && longitude2 !=0){
                checkingAddress = true;

                new BridgePointAsynTask().execute();
                new onlyPedestrianPointAsynTask().execute();
                new pedestrianPriorityPointAsynTask().execute();

                new SchoolZonePointAsynTask().execute();
                new CrosswalkPointAsynTask().execute();
                new FireTruckPointAsynTask().execute();
                new WaterFacilityPointAsynTask().execute();
            }else{
                Toast.makeText(this,"도착지를 잘못입력하셨습니다." ,Toast.LENGTH_LONG).show();
                checkingAddress = false;
            }
        }else{
            Toast.makeText(this,"출발지를 잘못입력하셨습니다." ,Toast.LENGTH_LONG).show();
            checkingAddress = false;
        }

    }

      /*
        함수명   : createMap
        간략     : Tmap 띄우기 위한 메소드
        상세     : 경로와 불법주차지역등 정보를 알려주기 위해 사용할 지도 설정및 생성
        작성자   : 이성재
        날짜     : 2021.06.07
        why      : 지도를 활용하기 위해서 만들었습니다.
     */

    public void createMap() {
        // GPS 사용 준비
        TMapGpsManager tMapGPS = new TMapGpsManager(this);

        // TMap GPS 설정
        tMapGPS.setMinTime(1); // 변경 인식시간
        tMapGPS.setMinDistance(1);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER); // 네트워크 방법
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER); // GPS방법 [위성기반] (작동 안 함)

        // GPS시작
        tMapGPS.OpenGps();


        // tMap 기본셋팅
        tMapView = new TMapView(this); // tMapView 객체 생성 (지도를 쓰기 위한)
        tMapView.setHttpsMode(true); // https 모드 허용해야 지도 뜸
        tMapView.setSKTMapApiKey("l7xxcffd8a912983400d81c75a095eb912e8"); // tMapView 기본셋팅 (앱키)

        // tMap 옵션추가
        tMapView.setSightVisible(true); // 어느 방향을 보고 있는지 보여줌
        tMapView.setCompassMode(true); // 자기가 바라보는 방향으로 돌아가는 나침판표시
        tMapView.setIconVisibility(true); //현재위치로 아이콘 표시 여부
        tMapView.setZoomLevel(17);

        ConstraintLayout constraintLayoutTmap = (ConstraintLayout) findViewById(R.id.search_result_map);
        constraintLayoutTmap.addView(tMapView); // 티맵을 레이아웃에 출력
    }

     /*
       함수명   : checkTagNumber
       간략     : 어떤 페이지에서 왔는지 확인하기 위해 사용
       상세     : 어떤 페이지에서 넘어왔는지 알아야 출발지와 도착지 정보를 받아올 수 있기 때문이다.
       작성자   : 이성재
       날짜     : 2021.06.05
       why      : Tag값을 이용해 출발지와 도착지 정보를 뿌리기 위해 사용했습니다.
    */

    public void checkTagNumber() {

        it = getIntent();
        tag = it.getIntExtra("it_tag", 0);

        if (tag == 1) {
            departureAddress = it.getStringExtra("currentAddress"); // 현재위치
            destinationAddress = it.getStringExtra("destinationAddress"); // 도착지

            TextView departureArea = findViewById(R.id.departureArea);
            departureArea.setText(departureAddress);
            TextView destinationArea = findViewById(R.id.destinationArea);
            destinationArea.setText(destinationAddress);
//            destinationPOIName = destination.getText().toString();
//            departurePOIName = departureArea.getText().toString();
        } else if (tag == 2) {
            departureAddress = it.getStringExtra("departureAddress"); // 현재위치
            destinationAddress = it.getStringExtra("destinationAddress"); // 도착지

            TextView departureArea = findViewById(R.id.departureArea);
            departureArea.setText(departureAddress);
            TextView destinationArea = findViewById(R.id.destinationArea);
            destinationArea.setText(destinationAddress);
        } else if (tag == 3) {
            departureAddress = it.getStringExtra("departureAddress"); // 현재위치
            destinationAddress = it.getStringExtra("destinationAddress"); // 도착지

            TextView departureArea = findViewById(R.id.departureArea);
            departureArea.setText(departureAddress);
            TextView destinationArea = findViewById(R.id.destinationArea);
            destinationArea.setText(destinationAddress);
        } else {
            departureAddress = it.getStringExtra("currentAddress");  // ParkingList에서 넘어온 현재 주소
            destinationAddress = it.getStringExtra("parkingName"); // ParkingList에서 넘어온 주차장이름
//            Double latitudeFromParkingList = it.getDoubleExtra("latitude",0); // ParkingList에서 넘어온 위도값
//            Double longitudeFromParkingList = it.getDoubleExtra("longitude",0); // ParkingList에서 넘어온 경도값

            TextView departureArea = findViewById(R.id.departureArea);
            departureArea.setText(departureAddress);
            TextView destinationArea = findViewById(R.id.destinationArea);
            destinationArea.setText(destinationAddress);
        }
    }



    // ---------------------------------------------------------화면 전환 메소드

      /*
        함수명   : enterDestinationSearchList
        간략     : 도착지를 검색하는 액티비티로 넘어갑니다.
        상세     : 도착지로 검색하는 액티비티에 적혀있는 현재위치정보와 적혀있는 도착지 정보를 보내고 넘어갑니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : v : 클릭한 뷰
        why      : 값을 같이 안 넘기면 검색하고 다시 돌아왔을 때 출발지 도착지에 저장된 값들이 사라지기 때문에 만들었습니다.
     */

    public void enterDestinationSearchList(View v) { // 도착지 검색화면으로 이동
        Intent it = new Intent(this, DestinationSearchListFrom.class);
        it.putExtra("departureAddress", departureAddress); // 현재위치 정보
        it.putExtra("destinationAddress", destinationAddress); // 도착지 정보
        startActivity(it);
    }

     /*
        함수명   : enterDepartureSearchList
        간략     : 출발지를 검색하는 액티비티로 넘어갑니다.
        상세     : 출발지로 검색하는 액티비티에 적혀있는 현재위치정보와 적혀있는 도착지 정보를 보내고 넘어갑니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : v : 클릭한 뷰
        why      : 값을 같이 안 넘기면 검색하고 다시 돌아왔을 때 출발지 도착지에 저장된 값들이 사라지기 때문에 만들었습니다.
     */

    public void enterDepartureSearchList(View v) { // 출발지 검색화면으로 이동
        Intent it = new Intent(this, DepartureSearchList.class);
        it.putExtra("departureAddress", departureAddress); // 현재위치 정보
        it.putExtra("destinationAddress", destinationAddress); // 도착지 정보
        startActivity(it);
    }

    /*
       함수명   : enterTmapNavi
       간략     : Tmap 네비게이션으로 연결합니다.
       상세     : 출발지, 도착지 이름과 위도 경도를 보내 그걸 기반으로 Tmap 네비게이션을 이용합니다.
       작성자   : 이성재
       날짜     : 2021.09.28
       param    : v : 클릭한 뷰
       why      : 네비게이션으로 길을 안내하기 위해 만들었습니다.
    */

    public void enterTmapNavi(View v) { // 출발지 검색화면으로 이동

        if(checkingAddress == true){
            Intent it = new Intent(this, TmapNavi.class);
            it.putExtra("departureAddress", departureAddress); // 현재위치명
            it.putExtra("latitude", latitude); // 현재위치 위도
            it.putExtra("longitude", longitude); // 현재위치 경도

            it.putExtra("destinationAddress", destinationAddress); // 도착지명
            it.putExtra("latitude2", latitude2); // 도착지 위도
            it.putExtra("longitude2", longitude2); // 도착지 경도

            startActivity(it);
        }else if (checkingAddress == false){
            Toast.makeText(this,"출발지나 도착지가 잘못 설정 되었습니다." ,Toast.LENGTH_LONG).show();
        }
    }

    /*
       함수명   : enterParkingLot
       간략     : 도착지 주변의 주차장을 안내해주는 페이지로 이동합니다.
       상세     : 도착지 주변의 주차장을 안내해주는 페이지에 도착지 값과 함께 전달해줍니다.
       작성자   : 이성재
       날짜     : 2021.09.28
       param    : v : 클릭한 뷰
       why      : 도착지 주변의 주차장을 안내해주기 위해 만들었습니다.
    */

    public void enterParkingLot(View v) { // 출발지 검색화면으로 이동

        if(checkingAddress == true){
            Intent it = new Intent(this, ParkingLotList.class);

            View parkingLotListButton = findViewById(R.id.parkingLotListButton);
            int tag =  Integer.parseInt(parkingLotListButton.getTag().toString()); // 태그로 어디에서 왔는지 구분하기 위해 사용

            it.putExtra("it_tag",tag); // 2번태그

            it.putExtra("departureAddress",departureAddress);
            it.putExtra("departureLatitude", latitude); // 도착지 위도
            it.putExtra("departureLongitude", longitude); // 도착지 경도

            it.putExtra("destinationAddress", destinationAddress); // 도착지명
            it.putExtra("destinationLatitude", latitude2); // 도착지 위도
            it.putExtra("destinationLongitude", longitude2); // 도착지 경도

            startActivity(it);
        }else if (checkingAddress == false){
            Toast.makeText(this,"출발지나 도착지가 잘못 설정 되었습니다." ,Toast.LENGTH_LONG).show();
        }

    }

     /*
       함수명   : makeMark
       간략     : 불법 주정차구역에 마커를 찍습니다.
       상세     : 도착지 1km내 어린이 보호 구역, 횡단보도, 소방차전용구역, 소방용수시설에 마커를 찍습니다.
       작성자   : 이성재
       날짜     : 2021.10.31
       param    : latitude : 불법주정차구역 위도, longitude : 불법주정차구역 경도, name : 불법주정차구역키값(키로 저장되기 때문에 구분하기 위해 만듬)
                  title : 풍선뷰 내용을 넣기 위해 사용
       why      : 1km 내에 있으면 불법주정차 구역에 마커를 찍기 위해 만들었습니다.
    */

    public void makeMark(double prohibition_latitude, double prohibition_longitude, String name,String title) {

        for (int i = 0; i < 1; i++) {
            if ((prohibition_latitude < (latitude2 + (PER_LATITUDE))) && prohibition_latitude > ((latitude2) - (PER_LATITUDE))) { // 1km 위도 범위내에 있으면
                if ((prohibition_longitude < (longitude2) + (PER_LATITUDE)) && prohibition_longitude > ((longitude2) - (PER_LONGITUDE))) {
                    TMapPoint tpoint = new TMapPoint(prohibition_latitude, prohibition_longitude);
                    // 마커 아이콘

                    Bitmap bitmap = BitmapFactory.decodeResource(thisContext.getResources(), R.drawable.marker);

                    TMapMarkerItem marker = new TMapMarkerItem();
                    marker.setIcon(bitmap); // 마커 아이콘 지정
                    marker.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    marker.setTMapPoint(tpoint); // 마커의 좌표 지정
                    marker.setName("Destination"); // 마커의 타이틀 지정
                    marker.setCanShowCallout(true); // 풍선뷰
                    marker.setCalloutTitle(title);

                    tMapView.addMarkerItem(name, marker); // 지도에 마커 추가
                } else {
                    continue;
                }
            } else {
                continue;// 1km 경도 범위내에 있으면
            }
        }
    }

     /*
       함수명   : makePolygon
       간략     : 불법주정차구역을 폴리곤으로 나타냅니다.
       상세     : 도착지 1km내 다리, 보행자전용도로, 보호자우선도로에 폴리곤으로 나타냅니다.
       작성자   : 이성재
       날짜     : 2021.10.30
       param    : startLat : 불법주정차구역 시작위도, startLong : 불법주정차구역 시작경도, endLat : 불법주정차구역 종료위도, endLong : 불법주정차구역 종료경도
                  name : 불법주정차구역키값(키로 저장되기 때문에 구분하기 위해 만듬)
       why      : 1km 내에 있으면 불법주정차 구역에 폴리곤으로 나타내기 위해 만들었습니다.
    */

    public void makePolygon(double startLat,double startLong, double endLat, double endLong, String name){

        for (int i = 0; i < 1; i++) {
            if ((startLat < (latitude2 + (PER_LATITUDE))) && startLat > ((latitude2) - (PER_LATITUDE))) { // 1km 위도 범위내에 있으면
                if ((startLong < (longitude2) + (PER_LATITUDE)) && startLong > ((longitude2) - (PER_LONGITUDE))) {
                    ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();

                    alTMapPoint.add(new TMapPoint(startLat, startLong)); // 시작하는 곳 위경도
                    alTMapPoint.add(new TMapPoint(endLat, endLong)); // 끝나는 곳 위경도

                    // 폴리곤 설정
                    TMapPolygon tMapPolygon = new TMapPolygon();
                    tMapPolygon.setLineColor(Color.RED);
                    tMapPolygon.setPolygonWidth(2);
                    tMapPolygon.setAreaColor(Color.RED);
                    tMapPolygon.setAreaAlpha(100);

                    for (int j = 0; j < alTMapPoint.size(); j++) {
                        tMapPolygon.addPolygonPoint(alTMapPoint.get(j));
                    }

                    tMapView.addTMapPolygon(name, tMapPolygon);
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }

    }

    // --------------------화면전환 메소드 ------------------------------------


     /*
        함수명   : onLocationChange
        간략     : 위치가 변할때마다 그 위치로 카메라를 이동시킵니다.
        상세     : 위치가 변할때마다 중심좌표로 이동하고 서클도 또한 그 위치로 이동합니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : location : 현재 위치
        why      : 현재위치로 이동하기위해 만들었습니다.
     */


    @Override
    public void onLocationChange(Location location) { // 이걸 써야 처음 좌표와 다른위치로 이동했다는걸 인식해 그 좌표를 가져와서 거기로 이동(콜백 메소드)
        if(locationButton){
            
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 서클을 현재 위치 중심좌표로 이동(좌표)
            tMapView.setCenterPoint(location.getLongitude(), location.getLatitude()); // 서클이 있는 곳으로 이동(현재위치로이동)
            
            Log.d("버튼이 켜졌습니다.","버튼이 켜졌습니다.");
        }else{
            Log.d("버튼이 꺼졌습니다.","버튼이 꺼졌습니다.");
        }
    }


     /*
        함수명   : changeLocationMode
        간략     : 자기 시점을 고정시킬지 아닐지에 대한 함수입니다.
        상세     : 버튼을 누르면 자기 시점에 고정되고 버튼을 한 번더 누르면 자유시점으로 바뀝니다.
        작성자   : 이성재
        날짜     : 2021.06.31
        param    : v : 버튼 클릭
        why      : 다른 장소를 보고 싶은데 자기 시점에 계속 고정되어서 다시 되돌아가는 상황을 고려해 마늗ㄹ었습니다.
     */

    public void changeLocationMode(View v) { // 위치가 변할 때마다 onLocation을 작동할지 말지에 대한 버튼리스너
        if (locationButton == true){
            locationButton = false;
            
            TMapPoint tpoint = tMapView.getLocationPoint();
            double currentLatitude = tpoint.getLatitude();
            double currentLongitude = tpoint.getLongitude();
            tMapView.setLocationPoint(currentLongitude, currentLatitude); // 서클을 현재 위치 중심좌표로 이동(좌표)
            tMapView.setCenterPoint(currentLongitude, currentLatitude); // 서클이 있는 곳으로 이동(현재위치로이동)

            Log.d("false입니다.","false입니다.");
        }else if(locationButton == false){
            locationButton = true;
            Log.d("true입니다.","true입니다.");
        }
    }

    // ----------------- 지도 관련 쓰레드 ------------------

         /*
           클래스명 : POIPointAsynTask
           간략     : 위도 경도를 가져오는 쓰레드입니다.
           상세     : 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
           작성자   : 이성재
           날짜     : 2021.06.05
         */


    class POIPointAsynTask extends AsyncTask<String, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식


     /*
        함수명   : onPostExecute
        간략     : 경로를 그리는 비동기방식을 실행시킵니다.
        상세     : 위도와 경도를 가져온 후 경로를 그리는 비동기방식을 실행시킵니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        why      : 출발지 도착지의 위도 경도 값을 가져와야지 경로를 그릴 수 있기 때문입니다.
     */

        @Override
        protected void onPostExecute(Void aVoid) {
            // 이 비동기방식이 끝나면 TmapAsyncTask가 실행된다.
            new TmapAsyncTask().execute(tMapView);

        }

      /*
        함수명   : doInBackground
        간략     : 출발지와 도착지의 위도와 경도를 알아냅니다.
        상세     : 출발지와 도착지의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : POIPoint : 출발지와 도착지의 주소를 배열로 받습니다.
        why      : 출발지와 도착지의 위도와 경도를 가져와야 경로를 그릴 수 있기 때문에 만들었습니다.
      */

        @Override
        protected Void doInBackground(String... POIPoint) {
            TMapData tmapdata = new TMapData();

            String point = null;
            ArrayList poiItem = null;

            try {
                poiItem = tmapdata.findTitlePOI(POIPoint[0]);
                TMapPOIItem item = (TMapPOIItem) poiItem.get(0);
                point = item.getPOIPoint().toString(); // 이름 좌표 등이 적혀있는 String 형의 point 객체
                String Points[] = point.split(" "); // 위도 경도로 분리하는 작업
                latitude = Double.parseDouble(Points[1]); // 위도
                longitude = Double.parseDouble(Points[3]); // 경도
                tMapPointStart = new TMapPoint(latitude, longitude); // 출발지 위경도

                ArrayList poiItem2 = tmapdata.findTitlePOI(POIPoint[1]); // doInBackground에 있는 String 배열에서 첫번째 인덱스
                TMapPOIItem item2 = (TMapPOIItem) poiItem2.get(0);
                String point2 = item2.getPOIPoint().toString(); // 이름 좌표 등이 적혀있는 String 형의 point 객체
                String Points2[] = point2.split(" "); // 위도 경도로 분리하는 작업
                latitude2 = Double.parseDouble(Points2[1]); // 위도
                longitude2 = Double.parseDouble(Points2[3]); // 경도
                tMapPointEnd = new TMapPoint(latitude2, longitude2); // 도착지 위경도

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            }
            return null;
        }
    }


         /*
           클래스명 : TmapAsyncTask
           간략     : 경로를 그리는 쓰레드입니다.
           상세     : 경로를 그리려면 메인 쓰레드에서 할 수 없기 때문입니다.
           작성자   : 이성재
           날짜     : 2021.06.05
         */


    class TmapAsyncTask extends AsyncTask<TMapView, Void, Void> { // 출발지 도착지 경로 알려주는 비동기방식


     /*
        함수명   : doInBackground
        간략     : 출발지와 도착지 경로를 그립니다.
        상세     : 출발지와 도착지 위도 경도를 매개변수로 받아와 경로를 그립니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        return   : TmapViews[0] : TmapView에 해당 내용을 적용시키기 위함
        param    : TmapViews : 위도와 경도 정보가 들어간 정보를 받아와서 그려야하기 때문에 매개변수로 받습니다.
        why      : 출발지와 도착지를 경로로 표시해 보기좋게 하기위해 만들었습니다.
     */

        @Override
        protected Void doInBackground(TMapView... TmapViews) {

            try {

                TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                // 경로를 만드는 tMapPolyLine 객체 생성과 출발지와 도착지 설정
                tMapPolyLine.setLineColor(Color.BLUE); // 경로 색상
                tMapPolyLine.setLineWidth(1); // 경로 선의 두께
                TmapViews[0].addTMapPolyLine("Line1", tMapPolyLine); // 경로를 그린다
//                    TmapViews[0].addTMapPath(tMapPolyLine); // 이거 안 쓰면 밑에 아이콘 사용 불가
//                    Bitmap start = decodeResource(getResources(),R.drawable.start); // 출발지 아이콘
//                    Bitmap end = decodeResource(getResources(),R.drawable.end); // 도착지 아이콘
//                    TmapViews[0].setTMapPathIcon(start, end); // 출발 도착 아이콘 설정

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }





//
//        class POIsearch extends AsyncTask<String,Void,TMapData>{ // 잠시 보류
//
//    /*
//        함수명   :
//        간략     :
//        상세     :
//        작성자   :
//        날짜     :
//        return   :
//        param    :
//        why      :
//
//     */
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//    /*
//        함수명   :
//        간략     :
//        상세     :
//        작성자   :
//        날짜     :
//        return   :
//        param    :
//        why      :
//
//     */
//
//            @Override
//            protected TMapData doInBackground(String... address_name) {
//                TMapData tmapdata = new TMapData();
//
//                  tmapdata.findAllPOI(address_name[0], new TMapData.FindAllPOIListenerCallback() {
//                    @Override // 왜 Override를 쓰는지 모르겠음 왜 이런형식이냐면 TMapData객체에 인터페이스를 매개변수로 받아서 new를 쓰는건가
//                    public void onFindAllPOI(ArrayList poiItem) {
//                        for(int i = 0; i < poiItem.size(); i++) {
//                            TMapPOIItem  item = (TMapPOIItem) poiItem.get(i);
//                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
//                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
//                                    "Point: " + item.getPOIPoint().toString());
//                            // 안에 반복문은 그냥 Log로 찍어내기 위한 것
//                        }
//                    }
//                });
//                return tmapdata;
//            }
//
//    /*
//        함수명   :
//        간략     :
//        상세     :
//        작성자   :
//        날짜     :
//        return   :
//        param    :
//        why      :
//
//     */
//
//            @Override
//            protected void onPostExecute(TMapData tmapdata) {
//                super.onPostExecute(tmapdata); // 그럼 여기서 바로 넘어가는 건가?
//            }
//        }

    // ------------------------------ json 파싱관련 메소드들 --------------------------

     /*
      함수명   : ReadTextFile
      간략     : 불법 주정차구역 json 파일을 읽어옵니다.
      상세     : 불법 주정차구역 json 파일을 읽어옵니다.
      작성자   : 이성재
      날짜     : 2021.10.28
      why      : txt파일에 들어있는 json 데이터를 읽어오기 위해서 만들었습니다.
    */

    public String ReadTextFile(String txtName) {
        StringBuffer strBuffer = new StringBuffer();
        try {
            InputStream is = getResources().openRawResource(
                    getResources().getIdentifier(txtName,
                            "raw", getPackageName()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line + "\n");
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return strBuffer.toString();
    }


    // ----------------- 마커 스레드 -----------------

    // 어린이보호구역 json 데이터

        /*
       클래스명 : schoolZonePointAsynTask
       간략     : 위도 경도를 가져오는 쓰레드입니다.
       상세     : 어린이 보호 구역의 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
       작성자   : 이성재
       날짜     : 2021.10.29
     */


    class SchoolZonePointAsynTask extends AsyncTask<Void, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식

      /*
        함수명   : doInBackground
        간략     : 어린이 보호 구역의 위도와 경도를 알아냅니다.
        상세     : 어린이 보호 구역의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.10.29
        why      : 위도와 경도를 가져와야 마커로 찍어서 표현할 수 있기 때문에 만들었습니다.
      */

        @Override
        protected Void doInBackground(Void... voids) {

            String schoolZoneJson = ReadTextFile(schoolZoneJsonNm);

            try {
                JSONObject schoolZoneJsonObject = new JSONObject(schoolZoneJson);
                JSONArray schoolZoneArray = schoolZoneJsonObject.getJSONArray("records");

                for (int i = 0; i < schoolZoneArray.length(); i++) {
                    try {
                        // 배열을 하나씩 읽기
                        JSONObject schoolZoneObject = schoolZoneArray.getJSONObject(i);

                        // 횡단보도 위도와 경도와 이름
                        String schoolZoneLatitude = schoolZoneObject.getString("위도");
                        String schoolZoneLongitude = schoolZoneObject.getString("경도");
//                        String schoolZoneName = schoolZoneObject.getString("대상시설명");

                        // 티맵에서 위도 경도를 이용할 수 있게 더블형으로 형변환 + 위도 경도값이 없을 때 스킵
                        try{
                            double schoolZoneLatitudeD = Double.parseDouble(schoolZoneLatitude);
                            double schoolZoneLongitudeD = Double.parseDouble(schoolZoneLongitude);
                            makeMark(schoolZoneLatitudeD, schoolZoneLongitudeD,"어린이보호구역"+i, "어린이보호구역"); // 겹칠 경우 데이터 누락이 없게
                        }catch (Exception e){
                            continue;
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


        // 횡단보도 json 데이터

        /*
       클래스명 : crosswalkPointAsynTask
       간략     : 위도 경도를 가져오는 쓰레드입니다.
       상세     : 횡단보도의 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
       작성자   : 이성재
       날짜     : 2021.10.29
     */


        class CrosswalkPointAsynTask extends AsyncTask<Void, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식

      /*
        함수명   : doInBackground
        간략     : 횡단보도의 위도와 경도를 알아냅니다.
        상세     : 횡단보도의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.10.29
        why      : 위도와 경도를 가져와야 마커로 찍어서 표현할 수 있기 때문에 만들었습니다.
      */

            @Override
            protected Void doInBackground(Void... voids) {

                String crosswalkJson = ReadTextFile(crosswalkJsonNm);

                try {
                    JSONObject crosswalkJsonObject = new JSONObject(crosswalkJson);
                    JSONArray crosswalkArray = crosswalkJsonObject.getJSONArray("records");

                    for (int i = 0; i < crosswalkArray.length(); i++) {
                        try {
                            // 배열을 하나씩 읽기
                            JSONObject crosswalkObject = crosswalkArray.getJSONObject(i);

                            // 횡단보도 위도와 경도
                            String crosswalkLatitude = crosswalkObject.getString("위도");
                            String crosswalkLongitude = crosswalkObject.getString("경도");

                            // 티맵에서 위도 경도를 이용할 수 있게 더블형으로 형변환 + 위도 경도값이 없을 때 스킵
                            try{
                                double crosswalkLatitudeD = Double.parseDouble(crosswalkLatitude);
                                double crosswalkLongitudeD = Double.parseDouble(crosswalkLongitude);
                                makeMark(crosswalkLatitudeD, crosswalkLongitudeD,"횡단보도"+i, "횡단보도"); // 겹칠 경우 데이터 누락이 없게
                            }catch (Exception e){
                                continue;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        // 소방차전용구역 json 데이터

        /*
       클래스명 : fireTruckPointAsynTask
       간략     : 위도 경도를 가져오는 쓰레드입니다.
       상세     : 소방차전용구역의 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
       작성자   : 이성재
       날짜     : 2021.10.29
     */


        class FireTruckPointAsynTask extends AsyncTask<Void, Void, String> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식

      /*
        함수명   : doInBackground
        간략     : 소방차전용구역의 위도와 경도를 알아냅니다.
        상세     : 소방차전용구역의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.10.29
        why      : 위도와 경도를 가져와야 마커로 찍어서 표현할 수 있기 때문에 만들었습니다.
      */

            @Override
            protected String doInBackground(Void... voids) {

                String fireTruckJson = ReadTextFile(fireTruckJsonNm);

                try {
                    JSONObject fireTruckJsonObject = new JSONObject(fireTruckJson);
                    JSONArray fireTruckArray = fireTruckJsonObject.getJSONArray("records");

                    for (int i = 0; i < fireTruckArray.length(); i++) {
                        try {
                            // 배열을 하나씩 읽기
                            JSONObject fireTruckObject = fireTruckArray.getJSONObject(i);

                            // 소방차전용구역 위도와 경도
                            String fireTruckLatitude = fireTruckObject.getString("위도");
                            String fireTruckLongitude = fireTruckObject.getString("경도");


                            // 티맵에서 위도 경도를 이용할 수 있게 더블형으로 형변환 + 위도 경도값이 없을 때 스킵
                            try{
                                double fireTruckLatitudeD = Double.parseDouble(fireTruckLatitude);
                                double fireTruckLongitudeD = Double.parseDouble(fireTruckLongitude);


                                makeMark(fireTruckLatitudeD, fireTruckLongitudeD,"소방차전용구역"+i, "소방차전용구역"); // 겹칠 경우 데이터 누락이 없게
                            }catch (Exception e){
                                continue;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return "ab";
            }
        }

        // 소방용수시설 json 데이터

        /*
       클래스명 : WaterFacilityPointAsynTask
       간략     : 위도 경도를 가져오는 쓰레드입니다.
       상세     : 소방용수시설의 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
       작성자   : 이성재
       날짜     : 2021.10.29
     */


        class WaterFacilityPointAsynTask extends AsyncTask<Void, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식

      /*
        함수명   : doInBackground
        간략     : 소방용수시설의 위도와 경도를 알아냅니다.
        상세     : 소방용수시설의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.10.29
        why      : 위도와 경도를 가져와야 마커로 찍어서 표현할 수 있기 때문에 만들었습니다.
      */

            @Override
            protected Void doInBackground(Void... voids) {

                String waterFacilityJson = ReadTextFile(firefightingWaterFacility);

                try {
                    JSONObject waterFacilityJsonObject = new JSONObject(waterFacilityJson);
                    JSONArray waterFacilityArray = waterFacilityJsonObject.getJSONArray("records");

                    for (int i = 0; i < waterFacilityArray.length(); i++) {
                        try {
                            // 배열을 하나씩 읽기
                            JSONObject waterFacilityObject = waterFacilityArray.getJSONObject(i);

                            // 소방용수시설 위도와 경도
                            String waterFacilityLatitude = waterFacilityObject.getString("위도");
                            String waterFacilityLongitude = waterFacilityObject.getString("경도");


                            // 티맵에서 위도 경도를 이용할 수 있게 더블형으로 형변환 + 위도 경도값이 없을 때 스킵
                            try{
                                double waterFacilityLatitudeD = Double.parseDouble(waterFacilityLatitude);
                                double waterFacilityLongitudeD = Double.parseDouble(waterFacilityLongitude);
                                makeMark(waterFacilityLatitudeD, waterFacilityLongitudeD, "소방용수시설"+i, "소방용수시설"); // 겹칠 경우 데이터 누락이 없게
                            }catch (Exception e){
                                continue;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        // ----------------- 폴리곤 스레드 -----------------

        // 다리 json 데이터

    /*
       클래스명 : bridgePointAsynTask
       간략     : 위도 경도를 가져오는 쓰레드입니다.
       상세     : 다리의 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
       작성자   : 이성재
       날짜     : 2021.10.29
     */


        class BridgePointAsynTask extends AsyncTask<Void, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식

      /*
        함수명   : doInBackground
        간략     : 다리의 출발지와 도착지의 위도와 경도를 알아냅니다.
        상세     : 다리의 출발지와 도착지의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.10.29
        why      : 출발지와 도착지의 위도와 경도를 가져와야 경로를 그릴 수 있기 때문에 만들었습니다.
      */

            @Override
            protected Void doInBackground(Void... voids) {

                int iterator = 0;
                String bridgeJson = ReadTextFile(bridgeJsonNm);

                try {
                    JSONObject bridgeJsonObject = new JSONObject(bridgeJson);
                    JSONArray bridgeArray = bridgeJsonObject.getJSONArray("records");

                    for (int i = 0; i < bridgeArray.length(); i++) {
                        try {
                            // 배열을 하나씩 읽기
                            JSONObject bridgeObject = bridgeArray.getJSONObject(i);

                            // 다리 시작 위치 (위도, 경도)
                            String startLatitude = bridgeObject.getString("교량시작점위도");
                            String startLongitude = bridgeObject.getString("교량시작점경도");

                            // 다리 끝나는 위치 (위도, 경도)
                            String endLatitude = bridgeObject.getString("교량종료점위도");
                            String endLongitude = bridgeObject.getString("교량종료점경도");

                            // 티맵에서 위도 경도를 이용할 수 있게 더블형으로 형변환 + 위도 경도값이 없을 때 스킵
                            try{
                                double startLatitudeD = Double.parseDouble(startLatitude);
                                double startLongitudeD = Double.parseDouble(startLongitude);
                                double endLatitudeD = Double.parseDouble(endLatitude);
                                double endLongitudeD = Double.parseDouble(endLongitude);

                                iterator++;
                                makePolygon(startLatitudeD, startLongitudeD, endLatitudeD, endLongitudeD,"교량"+i);

                            } catch(Exception e){
                                continue;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        // 보행자전용도로 json 데이터

    /*
       클래스명 : onlyPedestrianPointAsynTask
       간략     : 위도 경도를 가져오는 쓰레드입니다.
       상세     : 보호자전용도로의 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
       작성자   : 이성재
       날짜     : 2021.10.29
     */


        class onlyPedestrianPointAsynTask extends AsyncTask<Void, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식

      /*
        함수명   : doInBackground
        간략     : 보호자전용도로의 출발지와 도착지의 위도와 경도를 알아냅니다.
        상세     : 보호자전용도로의 출발지와 도착지의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.10.29
        why      : 출발지와 도착지의 위도와 경도를 가져와야 경로를 그릴 수 있기 때문에 만들었습니다.
      */

            @Override
            protected Void doInBackground(Void... voids) {

                String onlyPedestrianJson = ReadTextFile(onlyPedestrianJsonNm);

                try {
                    JSONObject onlyPedestrianJsonObject = new JSONObject(onlyPedestrianJson);
                    JSONArray onlyPedestrianArray = onlyPedestrianJsonObject.getJSONArray("records");

                    for (int i = 0; i < onlyPedestrianArray.length(); i++) {
                        try {
                            // 배열을 하나씩 읽기
                            JSONObject onlyPedestrianObject = onlyPedestrianArray.getJSONObject(i);

                            // 보호자전용도로 시작 위치 (위도, 경도)
                            String startLatitude = onlyPedestrianObject.getString("보행자전용도로시작점위도");
                            String startLongitude = onlyPedestrianObject.getString("보행자전용도로시작점경도");

                            // 보호자전용도로 끝나는 위치 (위도, 경도)
                            String endLatitude = onlyPedestrianObject.getString("보행자전용도로종료점위도");
                            String endLongitude = onlyPedestrianObject.getString("보행자전용도로종료점경도");

                            // 티맵에서 위도 경도를 이용할 수 있게 더블형으로 형변환 + 위도 경도값이 없을 때 스킵
                            try{
                                double startLatitudeD = Double.parseDouble(startLatitude);
                                double startLongitudeD = Double.parseDouble(startLongitude);
                                double endLatitudeD = Double.parseDouble(endLatitude);
                                double endLongitudeD = Double.parseDouble(endLongitude);
                                makePolygon(startLatitudeD,startLongitudeD,endLatitudeD,endLongitudeD,"보행자전용도로"+i);

                            } catch(Exception e){
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        // 보행자우선도로 json 데이터

    /*
       클래스명 : pedestrianPriorityPointAsynTask
       간략     : 위도 경도를 가져오는 쓰레드입니다.
       상세     : 보호자우선도로의 위도 경도를 가져오려면 메인 쓰레드에서 할 수 없기 때문입니다.
       작성자   : 이성재
       날짜     : 2021.10.29
     */


        class pedestrianPriorityPointAsynTask extends AsyncTask<Void, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식

      /*
        함수명   : doInBackground
        간략     : 보호자우선도로의 출발지와 도착지의 위도와 경도를 알아냅니다.
        상세     : 보호자우선도로의 출발지와 도착지의 위도와 경도를 알아냅니다.
        작성자   : 이성재
        날짜     : 2021.10.29
        why      : 출발지와 도착지의 위도와 경도를 가져와야 경로를 그릴 수 있기 때문에 만들었습니다.
      */

            @Override
            protected Void doInBackground(Void... voids) {

                String pedestrianPriorityJson = ReadTextFile(pedestrianPriorityJsonNm);

                try {
                    JSONObject pedestrianPriorityJsonObject = new JSONObject(pedestrianPriorityJson);
                    JSONArray pedestrianPriorityArray = pedestrianPriorityJsonObject.getJSONArray("records");

                    for (int i = 0; i < pedestrianPriorityArray.length(); i++) {
                        try {
                            // 배열을 하나씩 읽기
                            JSONObject pedestrianPriorityObject = pedestrianPriorityArray.getJSONObject(i);

                            // 보호자우선도로 시작 위치 (위도, 경도)
                            String startLatitude = pedestrianPriorityObject.getString("보행자우선도로시작점위도");
                            String startLongitude = pedestrianPriorityObject.getString("보행자우선도로시작점경도");

                            // 보호자우선도로 끝나는 위치 (위도, 경도)
                            String endLatitude = pedestrianPriorityObject.getString("보행자우선도로종료점위도");
                            String endLongitude = pedestrianPriorityObject.getString("보행자우선도로종료점경도");

                            // 티맵에서 위도 경도를 이용할 수 있게 더블형으로 형변환 + 위도 경도값이 없을 때 스킵
                            try{
                                double startLatitudeD = Double.parseDouble(startLatitude);
                                double startLongitudeD = Double.parseDouble(startLongitude);
                                double endLatitudeD = Double.parseDouble(endLatitude);
                                double endLongitudeD = Double.parseDouble(endLongitude);

                                makePolygon(startLatitudeD, startLongitudeD, endLatitudeD, endLongitudeD,"보행자우선도로"+i);

                            } catch(Exception e){
                                continue;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

    }





