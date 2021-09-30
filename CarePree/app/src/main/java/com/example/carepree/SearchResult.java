/*

@ breif  : 도착지 인근에 불법주정차구역과 길찾기기능이 있는 페이지
@ detail : 출발지와 도착지로 길찾기 표시를 해주고 얼마나 걸리는지와 도착지에 불법주정차구역이 있는지 알려주기 위해 만들었다.
@ why    : 출발지와 도착지를 기준으로 길찾기 기능과 도착지에 인근에 불법주정차구역이 어디있는지 알려주기위해 만들었다.


 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

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

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapPolygon;
import com.skt.Tmap.TMapView;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import static android.graphics.BitmapFactory.decodeResource;

public class SearchResult extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{


    TMapPoint tMapPointStart;
    TMapPoint tMapPointEnd ;
    TMapView tMapView;

    Intent it;

    int tag;

    double latitude, longitude, latitude2, longitude2;

    String destinationPOIName;
    String departurePOIName ;
    String destinationAddress;
    String departureAddress;

    Context thisContext =  this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        // 태그값 확인하고 값 받아오기
        checkTagNumber();

        createMap();


//        new POIsearch().execute("SKT타워");

        new POIPointAsynTask().execute(departureAddress,destinationAddress);
        new MarkAsyncTask().execute();
        new PoligonAsyncTask().execute();

    }

      /*
        함수명   : createMap
        간략     : Tmap 띄우기 위한 메소드
        상세     : 경로와 불법주차지역등 정보를 알려주기 위해 사용할 지도 설정및 생성
        작성자   : 이성재
        날짜     : 2021.06.07
        why      : 지도를 활용하기 위해서 만들었습니다.
     */

    public void createMap(){
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

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.search_result_map);
        linearLayoutTmap.addView(tMapView); // 티맵을 레이아웃에 출력
    }

     /*
       함수명   : checkTagNumber
       간략     : 어떤 페이지에서 왔는지 확인하기 위해 사용
       상세     : 어떤 페이지에서 넘어왔는지 알아야 출발지와 도착지 정보를 받아올 수 있기 때문이다.
       작성자   : 이성재
       날짜     : 2021.06.05
       why      : Tag값을 이용해 출발지와 도착지 정보를 뿌리기 위해 사용했습니다.
    */

    public void checkTagNumber(){

        it = getIntent();
        tag = it.getIntExtra("it_tag",0);

        if(tag == 1){
            departureAddress = it.getStringExtra("currentAddress"); // 현재위치
            destinationAddress = it.getStringExtra("destinationAddress"); // 도착지

            TextView departureArea = findViewById(R.id.departureArea);
            departureArea.setText(departureAddress);
            TextView destinationArea = findViewById(R.id.destinationArea);
            destinationArea.setText(destinationAddress);
//            destinationPOIName = destination.getText().toString();
//            departurePOIName = departureArea.getText().toString();
        }else if (tag == 2){
            departureAddress = it.getStringExtra("departureAddress"); // 현재위치
            destinationAddress = it.getStringExtra("destinationAddress"); // 도착지

            TextView departureArea = findViewById(R.id.departureArea);
            departureArea.setText(departureAddress);
            TextView destinationArea = findViewById(R.id.destinationArea);
            destinationArea.setText(destinationAddress);
        }else if (tag == 3){
            departureAddress = it.getStringExtra("departureAddress"); // 현재위치
            destinationAddress = it.getStringExtra("destinationAddress"); // 도착지

            TextView departureArea = findViewById(R.id.departureArea);
            departureArea.setText(departureAddress);
            TextView destinationArea = findViewById(R.id.destinationArea);
            destinationArea.setText(destinationAddress);
        }else {
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




    // ---------------------------------------------------------화면 전환 메소드

     /*
        함수명   : enterFavorites
        간략     : 즐겨찾기 페이지로 이동
        상세     : 즐겨찾기로 페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : View (클릭한 View객체)
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
        param    : View (클릭한 View객체)
        why      : 설정 페이지로 이동하기 위해 만들었습니다.
     */

    public void enterSettings(View v){ // 설정으로 이동
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }

     /*
        함수명   : enterDestinationSearchList
        간략     : 도착지를 검색하는 액티비티로 넘어갑니다.
        상세     : 도착지로 검색하는 액티비티에 적혀있는 현재위치정보와 적혀있는 도착지 정보를 보내고 넘어갑니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : v : 클릭한 뷰
        why      : 값을 같이 안 넘기면 검색하고 다시 돌아왔을 때 출발지 도착지에 저장된 값들이 사라지기 때문에 만들었습니다.
     */

    public void enterDestinationSearchList(View v){ // 도착지 검색화면으로 이동
        Intent it = new Intent(this, DestinationSearchListFrom.class);
        it.putExtra("departureAddress",departureAddress); // 현재위치 정보
        it.putExtra("destinationAddress",destinationAddress); // 도착지 정보
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

    public void enterDepartureSearchList(View v){ // 출발지 검색화면으로 이동
        Intent it = new Intent(this, DepartureSearchList.class);
        it.putExtra("departureAddress",departureAddress); // 현재위치 정보
        it.putExtra("destinationAddress",destinationAddress); // 도착지 정보
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

    public void enterTmapNavi(View v){ // 출발지 검색화면으로 이동

        Intent it = new Intent(this, TmapNavi.class);
        it.putExtra("departureAddress",departureAddress); // 현재위치명
        it.putExtra("latitude",latitude); // 현재위치 위도
        it.putExtra("longitude",longitude); // 현재위치 경도

        it.putExtra("destinationAddress",destinationAddress); // 도착지명
        it.putExtra("latitude2",latitude2); // 도착지 위도
        it.putExtra("longitude2",longitude2); // 도착지 경도

        Log.d("테스트",departureAddress);
        Log.d("테스트",destinationAddress);
        Log.d("테스트","경도" + latitude);
        Log.d("테스트","경도2" + latitude2);
        Log.d("테스트","위도" + longitude);
        Log.d("테스트","위도2" + longitude2);
        startActivity(it);
    }

    // --------------------------------------------------------화면전환 메소드


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

        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 서클을 현재 위치 중심좌표로 이동(좌표)
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude()); // 서클이 있는 곳으로 이동(현재위치로이동)

    }



    // 지도 관련 쓰레드

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
                    tMapPointStart = new TMapPoint(latitude,longitude); // 출발지 위경도
                    
                    ArrayList poiItem2 = tmapdata.findTitlePOI(POIPoint[1]); // doInBackground에 있는 String 배열에서 첫번째 인덱스
                    TMapPOIItem item2= (TMapPOIItem) poiItem2.get(0);
                    String point2 = item2.getPOIPoint().toString(); // 이름 좌표 등이 적혀있는 String 형의 point 객체
                    String Points2[] = point2.split(" "); // 위도 경도로 분리하는 작업
                    latitude2 = Double.parseDouble(Points2[1]); // 위도
                    longitude2 = Double.parseDouble(Points2[3]); // 경도
                    tMapPointEnd = new TMapPoint(latitude2,longitude2); // 도착지 위경도

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    new POIPointAsynTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,destinationPOIName,departurePOIName);
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


        class TmapAsyncTask extends AsyncTask<TMapView,Void,Void> { // 출발지 도착지 경로 알려주는 비동기방식


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
            protected Void doInBackground(TMapView ... TmapViews) {

                try {

                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                    // 경로를 만드는 tMapPolyLine 객체 생성과 출발지와 도착지 설정
                    tMapPolyLine.setLineColor(Color.RED); // 경로 색상
                    tMapPolyLine.setLineWidth(1); // 경로 선의 두께
                    TmapViews[0].addTMapPolyLine("Line1", tMapPolyLine); // 경로를 그린다
//                    TmapViews[0].addTMapPath(tMapPolyLine); // 이거 안 쓰면 밑에 아이콘 사용 불가
//                    Bitmap start = decodeResource(getResources(),R.drawable.start); // 출발지 아이콘
//                    Bitmap end = decodeResource(getResources(),R.drawable.end); // 도착지 아이콘
//                    TmapViews[0].setTMapPathIcon(start, end); // 출발 도착 아이콘 설정

                }catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }


        }




    class MarkAsyncTask extends AsyncTask<TMapView,Void,Void> { // 출발지 도착지 경로 알려주는 비동기방식


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
        protected Void doInBackground(TMapView ... TmapViews) {

                TMapPoint tpoint = new TMapPoint(37.570841, 126.985302);
                // 마커 아이콘

                Bitmap bitmap = BitmapFactory.decodeResource(thisContext.getResources(), R.drawable.marker);

                TMapMarkerItem marker = new TMapMarkerItem();
                marker.setIcon(bitmap); // 마커 아이콘 지정
                marker.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                marker.setTMapPoint( tpoint ); // 마커의 좌표 지정
                marker.setName("Destination"); // 마커의 타이틀 지정
                marker.setCanShowCallout(true); // 풍선뷰
                marker.setCalloutTitle("Hello.World");

                tMapView.addMarkerItem("markerItem", marker); // 지도에 마커 추가
                tMapView.setCenterPoint( 126.985302, 37.570841 );

                return null;
            }

    }
    class PoligonAsyncTask extends AsyncTask<TMapView,Void,Void> { // 출발지 도착지 경로 알려주는 비동기방식


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
        protected Void doInBackground(TMapView ... TmapViews) {

            ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
            alTMapPoint.add( new TMapPoint(37.570841, 126.985302) ); // SKT타워
            alTMapPoint.add( new TMapPoint(37.551135, 126.988205) ); // N서울타워

            TMapPolygon tMapPolygon = new TMapPolygon();
            tMapPolygon.setLineColor(Color.BLUE);
            tMapPolygon.setPolygonWidth(2);
            tMapPolygon.setAreaColor(Color.GRAY);
            tMapPolygon.setAreaAlpha(100);
            for( int i=0; i<alTMapPoint.size(); i++ ) {
                tMapPolygon.addPolygonPoint( alTMapPoint.get(i) );
            }
            tMapView.addTMapPolygon("Line1", tMapPolygon);
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
    }




