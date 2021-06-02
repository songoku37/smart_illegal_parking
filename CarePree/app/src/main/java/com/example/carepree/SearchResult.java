package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;

import static android.graphics.BitmapFactory.decodeResource;

public class SearchResult extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    TextView destination;
    TextView departureArea;
    String destinationName;
    String destinationNameFromArrival;
    String departureAreaName;
    TMapPoint tMapPointStart;
    TMapPoint tMapPointEnd ;
    Intent it;
    String tag;
    TMapView tMapView;
    String startPoint ;
    String endPoint;
    String tempDepartureArea;
    String tempDestination;
    String tempId = "SKT타워";
    String destinationPOIName;
    String departurePOIName ;
    String currentAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // GPS 사용 준비
        TMapGpsManager tMapGPS = new TMapGpsManager(this);

        // TMap GPS 설정
        tMapGPS.setMinTime(1); // 변경 인식시간
        tMapGPS.setMinDistance(1);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER); // 네트워크 방법
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER); // GPS방법 [위성기반] (작동 안 함)

        // GPS시작
        tMapGPS.OpenGps();

        it = getIntent();
        tag = it.getStringExtra("it_tag"); // 어떤 페이지로 넘어왔는지 알기 위해 tag값을 씀
        currentAddress = it.getStringExtra("currentAddress");

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        // onPause에 저장되어 있는 값을 가져오기 위한 객체

        if(pref != null) // 저장된 데이터가 있는경우
        {
            if(tag.equals("3")){
                startPoint = pref.getString("tempStartPoint","서울랜드"); // 3번태그에서 올 경우 출발지값을 저장한 걸 가져와야함
                // tempStartPoint 속성에 들어있는 값을 가져오고 default값은 롯데월드
            }else if (tag.equals("2")) {
                endPoint = pref.getString("tempEndPoint","에버랜드"); // 2번태그에서 올 경우 도착지값을 저장한 걸 가져와야함
            }
        }


        // tMap 기본셋팅
        tMapView = new TMapView(this); // tMapView 객체 생성 (지도를 쓰기 위한)
        tMapView.setHttpsMode(true); // https 모드 허용해야 지도 뜸
        tMapView.setSKTMapApiKey("l7xxcffd8a912983400d81c75a095eb912e8"); // tMapView 기본셋팅 (앱키)

        // tMap 옵션추가
        tMapView.setSightVisible(true); // 어느 방향을 보고 있는지 보여줌
        tMapView.setCompassMode(true); // 자기가 바라보는 방향으로 돌아가는 나침판표시
        tMapView.setIconVisibility(true); //현재위치로 아이콘 표시 여부
        tMapView.setZoomLevel(17);




        new POIsearch().execute("SKT타워");

//        new FirstMapAsynTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // exeucteOnExecutor를 쓰면 동시다발적으로 일어난다
        new FirstMapAsynTask().execute(); // execute를 쓰면 순차적으로 비동기가 실행 됨


//      new TmapAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,tMapView);
        new TmapAsyncTask().execute(tMapView);


        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.search_result_map);
        linearLayoutTmap.addView(tMapView); // 티맵을 레이아웃에 출력




    }
    @Override
    protected void onPause() { // 액티비티 전환하기를 누르면 onPause가 콜백 됨
        super.onPause();

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        // 객체 이름은 불러올 때 사용되는 이름과 동일한 이름을 사용해야한다.
        SharedPreferences.Editor editor = pref.edit();
        // 필요한 정보를 저장할 수 있는 editor 생성

        if(tempId.equals("departureArea")){ // 출발지 검색화면으로 넘어가기 전에 도착지 정보를 저장
            tempDestination = destination.getText().toString();
            editor.putString("tempEndPoint", tempDestination);
        }else if (tempId.equals("destination")) { // 도착지 검색화면으로 넘어가기 전에 출발지 정보를 저장
            tempDepartureArea = departureArea.getText().toString();
            editor.putString("tempStartPoint", tempDepartureArea);
        }

        // 적용시키기
        editor.commit();
    }


    // ---------------------------------------------------------화면 전환 메소드

    public void enterFavorites(View v){ // 즐겨찾기 이동
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

    public void enterSearchListFromArrival(View v){ // 도착지 검색화면으로 이동
        tempId = v.getResources().getResourceEntryName(v.getId()); // Id값을 이용해 도착지 정보를 저장할지 출발지 정보를 저장할지 결정
        Intent it = new Intent(this,SearchListFromArrival.class);
        startActivity(it);
    }

    public void enterSearchListFromDepature(View v){ // 출발지 검색화면으로 이동
        tempId = v.getResources().getResourceEntryName(v.getId()); // Id값을 이용해 도착지 정보를 저장할지 출발지 정보를 저장할지 결정
        Intent it = new Intent(this,SearchListFromDeparture.class);
        startActivity(it);
    }

    // --------------------------------------------------------화면전환 메소드

    public void swapDepartureWithDestination(View v){ // 도착지랑 출발지를 바꾸는 메소드
        destination = findViewById(R.id.destination);
        departureArea = findViewById(R.id.departureArea);
        String destinationName = destination.getText().toString();
        String departureAreaName = departureArea.getText().toString();
        String temp ;

        temp = destinationName;
        destinationName = departureAreaName;
        departureAreaName = temp;

        destination.setText(destinationName);
        departureArea.setText(departureAreaName);
    }

    @Override
    public void onLocationChange(Location location) {
        // 이걸 써야 처음 좌표와 다른위치로 이동했다는걸 인식해 그 좌표를 가져와서 거기로 이동(콜백 메소드)
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude()); // 서클을 현재 위치 중심좌표로 이동(좌표)
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude()); // 서클이 있는 곳으로 이동(현재위치로이동)
    }


    class FirstMapAsynTask extends AsyncTask<Void,Void,Void>{ // 지도 출력과 자동 길찾기를 해준다
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            new POIPointAsynTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,destinationPOIName,departurePOIName);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (tag.equals("1")) { // SearchList에서 온 경우
                destinationName = it.getStringExtra("destinationText");
                destination = findViewById(R.id.destination);
                destination.setText(destinationName);
                // 도착지에 값을 뿌림

                if(departureArea == null){
                    departureArea = findViewById(R.id.departureArea);
                    departureArea.setText(currentAddress); // default 출발지 값 :여기에는 현 위치가 들어가면 된다.
                }   // 현 위치의 값을 받아와 현 위치에 뿌리기

            } else if (tag.equals("2")) {
                destination = findViewById(R.id.destination);
                destination.setText(endPoint);
                // onPause에 저장된 도착지 이름을 뿌린다

                departureAreaName = it.getStringExtra("departureAreaText");
                departureArea = findViewById(R.id.departureArea);
                departureArea.setText(departureAreaName);
                // 검색한 출발지 이름을 뿌린다

            } else if (tag.equals("3")) {
                departureArea = findViewById(R.id.departureArea);
                departureArea.setText(startPoint);
                // onPause에 저장된 출발지이름을 뿌린다

                destinationNameFromArrival = it.getStringExtra("destinationTextFromArrival");
                destination = findViewById(R.id.destination);
                destination.setText(destinationNameFromArrival);
                // 검색한 도착지이름을 뿌린다

            }
            destinationPOIName = destination.getText().toString(); // 밖에서 값을 받으면 nullPoint가 나오니까 여기에서 값을 받음
            departurePOIName = departureArea.getText().toString();
            return null;
        }

    }

        class POIPointAsynTask extends AsyncTask<String, Void, Void> { // 가장 결과값이 유사한 좌표 가져오는 비동기방식
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
              new TmapAsyncTask().execute(tMapView);
              // 이 비동기방식이 끝나면 TmapAsyncTask가 실행된다.
              super.onPostExecute(aVoid);
            }

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
                    double latitude = Double.parseDouble(Points[1]); // 위도
                    double longitude = Double.parseDouble(Points[3]); // 경도
                    tMapPointStart = new TMapPoint(latitude,longitude);
                    // 
                    
                    ArrayList poiItem2 = tmapdata.findTitlePOI(POIPoint[1]); // doInBackground에 있는 String 배열에서 첫번째 인덱스
                    TMapPOIItem item2= (TMapPOIItem) poiItem2.get(0);
                    String point2 = item2.getPOIPoint().toString(); // 이름 좌표 등이 적혀있는 String 형의 point 객체
                    String Points2[] = point2.split(" "); // 위도 경도로 분리하는 작업
                    double latitude2 = Double.parseDouble(Points2[1]); // 위도
                    double longitude2 = Double.parseDouble(Points2[3]); // 경도
                    tMapPointEnd = new TMapPoint(latitude2,longitude2);
                    // 도착지 위경도
                    

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



        class TmapAsyncTask extends AsyncTask<TMapView,Void,TMapView> { // 출발지 도착지 경로 알려주는 비동기방식


            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected TMapView doInBackground(TMapView ... TmapViews) {

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
                return TmapViews[0];
            }


            @Override
            protected void onPostExecute(TMapView TmapViews) {
                super.onPostExecute(TmapViews);
                // super.onPostExecute는 doInBackground에서 값이 안 넘어와도 무조건 써야함 void값으로 매개변수 넣어도 작동함
            }

        }

        class POIsearch extends AsyncTask<String,Void,TMapData>{ // 잠시 보류
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected TMapData doInBackground(String... address_name) {
                TMapData tmapdata = new TMapData();

                  tmapdata.findAllPOI(address_name[0], new TMapData.FindAllPOIListenerCallback() {
                    @Override // 왜 Override를 쓰는지 모르겠음 왜 이런형식이냐면 TMapData객체에 인터페이스를 매개변수로 받아서 new를 쓰는건가
                    public void onFindAllPOI(ArrayList poiItem) {
                        for(int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem  item = (TMapPOIItem) poiItem.get(i);
                            Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                                    "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                                    "Point: " + item.getPOIPoint().toString());
                            // 안에 반복문은 그냥 Log로 찍어내기 위한 것
                        }
                    }
                });
                return tmapdata;
            }

            @Override
            protected void onPostExecute(TMapData tmapdata) {
                super.onPostExecute(tmapdata); // 그럼 여기서 바로 넘어가는 건가?
            }
        }
    }




