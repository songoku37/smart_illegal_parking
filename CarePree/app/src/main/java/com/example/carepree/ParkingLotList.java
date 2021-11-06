/*

@ breif  : 인근 주차장 목록을 보여주는 페이지
@ detail : 2km 내 주차장들의 목록을 보여주는 페이지이다.
@ why    : 주차장을 못찾을 때 인근 주차장이 뭐가 있는지 알려주기 위해 만들었다.

 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParkingLotList extends AppCompatActivity {

    final double PER_LATITUDE = 0.0090909090909091; // 1km당 위도
    final double PER_LONGITUDE = 0.0112688753662384; // 1km당 경도
    double currentLatitude = 0;
    double currentLongitude = 0;
    double destinationLatitude = 0;
    double destinationLongitude = 0;

    int iter = 0;
    int checkingTag;
    String resultCode = "";
    String currentAddress;
    String destinationAddress;

    List<NearbyParkingLotSetterGetter> parkingArray = new ArrayList<NearbyParkingLotSetterGetter>(100);

    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);

//        currentLatitude = 36.785914; // 임시데이터
//        currentLongitude = 127.5838292988; // 임시데이터

        getParameter();

        mProgress = (ProgressBar) findViewById(R.id.progressBar); // 프로그레스 바

        ExecutorService THREAD_POOL = Executors.newFixedThreadPool(67); // 멀티쓰레드 설정
        for (int i = 0 ; i <= 67 ; i++){
            new ParkingListAsync().executeOnExecutor(THREAD_POOL, i);
        } // 공공데이터 자료 가져오는 멀티쓰레드
    }

     /*
        함수명   : getParameter
        간략     : 현재위치정보를 보낸 걸 받아옵니다.
        상세     : 현재 위치 주소와 위도경도값을 받아옵니다.
        작성자   : 이성재
        날짜     : 2021.06.07
        why      : navigation 아이콘을 눌렀을때 출발지와 도착지에 뿌리기 위한 정보를 받아오기위해 만들었습니다.
     */

     /*
        함수명   : getParameter
        간략     : 현재위치정보를 보낸 걸 받아옵니다.
        상세     : 1번태그이면 현재 위치 주소와 위도경도값을 받아옵니다. 2번태그이면 도착지의 주소와 위도 경도값을 받아옵니다.
        작성자   : 이성재
        날짜     : 2021.06.07
        why      : navigation 아이콘을 눌렀을때 출발지와 도착지에 뿌리기 위한 정보를 받아오기위해 만들었습니다.
     */

    public void getParameter(){

        Intent it = getIntent();

        checkingTag = it.getIntExtra("it_tag", 0);
        Log.d("checkingTag", String.valueOf(checkingTag));
        if(checkingTag == 1){
            currentAddress = it.getStringExtra("currentAddress");
            currentLatitude = it.getDoubleExtra("currentLatitude",0);
            currentLongitude = it.getDoubleExtra("currentLongitude",0);
        }else if (checkingTag == 2){

            currentAddress = it.getStringExtra("departureAddress");
            currentLatitude = it.getDoubleExtra("departureLatitude",0);
            currentLongitude = it.getDoubleExtra("departureLongitude",0);

            destinationAddress = it.getStringExtra("destinationAddress");
            destinationLatitude = it.getDoubleExtra("destinationLatitude",0);
            destinationLongitude = it.getDoubleExtra("destinationLongitude",0);

            Log.d("currentAddress",currentAddress);
            Log.d("currentLongitude", String.valueOf(currentLongitude));
            Log.d("currentLatitude", String.valueOf(currentLatitude));
            Log.d("destinationAddress",destinationAddress);
            Log.d("destinationLongitude", String.valueOf(destinationLongitude));
            Log.d("destinationLatitude", String.valueOf(destinationLatitude));
        }

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
        param    : v :클릭한 View객체
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
        param    : v :클릭한 View객체
        why      : 설정 페이지로 이동하기 위해 만들었습니다.
     */

    public void enterSettings(View v){ // 설정으로 이동
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }

     /*
        함수명   : findNearbyParkingLot
        간략     : 1km 범위내 주차장인지 확인하기 위한 메소드
        상세     : 받아온 주차장 정보들이 많은데 내 현재위치에서 1km 범위내에 주차장이면 True를 넘긴다.
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : latitude : 해당 주차장의 위도 , longitude : 해당 주차장의 경도
        why      : 설정 페이지로 이동하기 위해 만들었습니다.
     */

    public boolean findNearbyParkingLot(String latitude,String longitude){ // 1km 범위내에 있는지 확인하기 위한 메소드

        double dLatitude = Double.parseDouble(latitude);
        double dLongitude = Double.parseDouble(longitude);
        boolean judgeSwi = false;

        if(checkingTag==1){
            if((dLatitude < (currentLatitude + (PER_LATITUDE))) && dLatitude > ((currentLatitude) - (PER_LATITUDE))){ // 1km 위도 범위내에 있으면
                if((dLongitude < (currentLongitude) + (PER_LATITUDE)) && dLongitude > ((currentLongitude) - (PER_LONGITUDE))){ // 1km 경도 범위내에 있으면
                    judgeSwi = true;
                    return judgeSwi;
                }else{
                    judgeSwi = false;
                    return judgeSwi;
                }}
            else{
                judgeSwi = false;
                return judgeSwi;

            }
        }else if (checkingTag == 2){
            Log.d("checkingTag", String.valueOf(checkingTag));
            if((dLatitude < (destinationLatitude + (PER_LATITUDE))) && dLatitude > ((destinationLatitude) - (PER_LATITUDE))){ // 1km 위도 범위내에 있으면
                if((dLongitude < (destinationLongitude) + (PER_LATITUDE)) && dLongitude > ((destinationLongitude) - (PER_LONGITUDE))){ // 1km 경도 범위내에 있으면
                    judgeSwi = true;
                    return judgeSwi;
                }else{
                    judgeSwi = false;
                    return judgeSwi;
                }}
            else{
                judgeSwi = false;
                return judgeSwi;

            }
        }
        return judgeSwi;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    // 개발중...



    class NewRunnable implements Runnable {

        /*
            함수명   : run
            간략     : UI처리하라는 메소드
            상세     : runnable 객체를 받아와서 UI에서 처리하라는 메소드
            작성자   : 이성재
            날짜     : 2021.06.05
            why      : 주차장 정보를 받아오고 나서 그 자료 정보를 동적으로 레이아웃을 만들어야하기 때문에
       */

        @Override
        public void run() {
            runOnUiThread(runnable) ;
            // Runnable 객체에 구현된 코드를 반드시 메인 스레드에서 실행해야 할 때 사용하는 메서드
        }
    }

     /*
        함수명   : makeDynamicOutsideLinearLayout
        간략     : 주차장정보가 들어가는 큰 레이아웃 동적생성
        상세     : 하나의 주차장 정보들이 들어갈 수 있는 큰 리니어레이아웃 동적생성
        작성자   : 이성재
        날짜     : 2021.06.05
        return   : 어떤 리니어레이아웃을 만들지에 대한 설정을 적용시켜서 return값으로 내보냄
        why      : Runnable에 run()메소드 오버라이드하는 곳에 한번에 넣는것보다 이렇게 관리하는게 좋기 때문이다.
     */


    public LinearLayout makeDynamicOutsideLinearLayout(){

        LinearLayout parkingListInner = new LinearLayout(ParkingLotList.this); // ParkingLotList라는 xml에 dynamicLayout이라는 이름으로 만듦
        parkingListInner.setOrientation(LinearLayout.VERTICAL); // Orientation을 수평으로
        parkingListInner.setGravity(Gravity.CENTER); // 가운데 정렬
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); // 너비 높이 조정
        parkingListInner.setLayoutParams(params); // 적용

        return parkingListInner;
    }

     /*
        함수명   : makeDynamicOutsideLinearLayout2
        간략     : 주차장정보가 들어가는 큰 레이아웃 동적생성
        상세     : makeDynamicOutsideLinearLayout안에 들어가는 리니어 레이아웃 생성
        작성자   : 이성재
        날짜     : 2021.06.07
        return   : 어떤 리니어레이아웃을 만들지에 대한 설정을 적용시켜서 return값으로 내보냄
        why      : Runnable에 run()메소드 오버라이드하는 곳에 한번에 넣는것보다 이렇게 관리하는게 좋기 때문이다.
     */

    public LinearLayout makeDynamicOutsideLinearLayout2(){

        LinearLayout parkingListInner = new LinearLayout(ParkingLotList.this); // ParkingLotList라는 xml에 dynamicLayout이라는 이름으로 만듦
        parkingListInner.setOrientation(LinearLayout.HORIZONTAL); // Orientation을 수직으로
        parkingListInner.setGravity(Gravity.CENTER); // 가운데 정렬
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); // 너비 높이 조정
        parkingListInner.setLayoutParams(params); // 적용

        return parkingListInner;
    }
    
     /*
        함수명   : makeDynamicInsideLinearLayout
        간략     : 주차장사진과 주차장이름이 들어가는 리니어 레이아웃 동적생성
        상세     : 하나의 주차장사진과 주차장이름이 들어가는 리니어 레이아웃 동적생성
        작성자   : 이성재
        날짜     : 2021.06.05
        return   : 어떤 리니어레이아웃을 만들지에 대한 설정을 적용시켜서 return값으로 내보냄
        why      : Runnable에 run()메소드 오버라이드하는 곳에 한번에 넣는것보다 이렇게 관리하는게 좋기 때문이다.
     */


    public LinearLayout makeDynamicInsideLinearLayout(){

        String tag = String.valueOf(iter);

        LinearLayout parkingListInnerInner = new LinearLayout(ParkingLotList.this);
        parkingListInnerInner.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 80;
        parkingListInnerInner.setGravity(Gravity.CENTER); // 가운데 정렬
        parkingListInnerInner.setLayoutParams(params);
        parkingListInnerInner.setTag(tag); // 태그 설정

        parkingListInnerInner.setOnClickListener(new View.OnClickListener() { // onClick 생성해 주차장정보를 넘김
            public void onClick(View v) {

                String clickTagNumber = (String)parkingListInnerInner.getTag(); // Tag 값으로 ArrayList에 몇번째인지 파악한다.
                int tagNumber = Integer.parseInt(clickTagNumber); // String형태인 Tag값을 Index값을 지정해주기 위해서 사용

                if (checkingTag == 1){
                    Intent it = new Intent(ParkingLotList.this,ParkingLotInformation.class);
                    it.putExtra("parkingInformationsArrays", parkingArray.get(tagNumber)); // 해당 주차장에 대한 정보가 들어간 ArrayList를 넘김
                    it.putExtra("currentAddress",currentAddress); // 현재 위치 주소를 보냅니다.
                    it.putExtra("currentLatitude",currentLatitude); // 현재 위치 위도를 보냅니다.
                    it.putExtra("currentLongitude",currentLongitude); // 현재 위치 경도를 보냅니다.
                    startActivity(it);
                }else if (checkingTag == 2){
                    Intent it = new Intent(ParkingLotList.this,ParkingLotInformation.class);
                    it.putExtra("parkingInformationsArrays", parkingArray.get(tagNumber)); // 해당 주차장에 대한 정보가 들어간 ArrayList를 넘김
                    it.putExtra("currentAddress",destinationAddress); // 현재 위치 주소를 보냅니다.
                    it.putExtra("currentLatitude",destinationLatitude); // 현재 위치 위도를 보냅니다.
                    it.putExtra("currentLongitude",destinationLongitude); // 현재 위치 경도를 보냅니다.
                    startActivity(it);
                }

            }
        });

        return parkingListInnerInner;
    }

     /*
        함수명   : makeDynamicTxt
        간략     : 주차장 이름이 들어가는 텍스트뷰 동적생성
        상세     : 하나의 주차장 이름이 들어가는 텍스트뷰 동적생성
        작성자   : 이성재
        날짜     : 2021.06.05
        return   : 어떤 텍스트뷰을 만들지에 대한 설정을 적용시켜서 return값으로 내보냄
        why      : Runnable에 run()메소드 오버라이드하는 곳에 한번에 넣는것보다 이렇게 관리하는게 좋기 때문이다.
     */

    public TextView makeDynamicTxt(){
        String dynamicParkingName = parkingArray.get(iter).getPrkplceNm();
        TextView parkingName = new TextView(ParkingLotList.this); // width와 height 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        parkingName.setGravity(Gravity.CENTER); // 가운데 정렬
        params.setMargins(50,0,0,10);
        parkingName.setLayoutParams(params);
        parkingName.setText(dynamicParkingName);
        return parkingName;
    }

     /*
        함수명   : makeDynamicParkinglotImage
        간략     : 주차장이미지가 들어갈 이미지뷰 동적생성
        상세     : 하나의 주차장이미지가 들어갈 이미지뷰 동적생성
        작성자   : 이성재
        날짜     : 2021.06.05
        return   : 어떤 이미지뷰를 만들지에 대한 설정을 적용시켜서 return값으로 내보냄
        why      : Runnable에 run()메소드 오버라이드하는 곳에 한번에 넣는것보다 이렇게 관리하는게 좋기 때문이다.
     */


    public ImageView makeDynamicParkinglotImage(){
        ImageView parkingImage = new ImageView(ParkingLotList.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,350);
        params.setMargins(50,0,0,0);
        parkingImage.setImageResource(R.drawable.parkinglotimg);
        parkingImage.setLayoutParams(params);

        return parkingImage;
    }

     /*
        함수명   : makeDynamicNavigationImage
        간략     : 길찾기로 바로 갈 수있는 이미지뷰 생성
        상세     : 클릭했을 때 해당 주차장으로 길찾기로 이어지는 이미지뷰를 동적 생성한다.
        작성자   : 이성재
        날짜     : 2021.06.05
        return   : 어떤 이미지뷰를 만들지에 대한 설정을 적용시켜서 return값으로 내보냄
        why      : Runnable에 run()메소드 오버라이드하는 곳에 한번에 넣는것보다 이렇게 관리하는게 좋기 때문이다.
     */

    public ImageView makeDynamicNavigationImage(LinearLayout parkingListInnerInner){
        String tag = String.valueOf(iter);
        String dynamicParkingName = parkingArray.get(iter).getPrkplceNm();

        ImageView navigation = new ImageView(ParkingLotList.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,100);
        params.bottomMargin = 20; // 정렬이 조금 애매해서 주었음
        params.weight = 20;
        navigation.setTag(tag);
        navigation.setLayoutParams(params);
        navigation.setImageResource(R.drawable.ic_getdirections);


        navigation.setOnClickListener(new View.OnClickListener() { // onClick 생성해 주차장 위도 경도를 넘김
            public void onClick(View v) {
                String clickTagNumber = (String)parkingListInnerInner.getTag(); // Tag 값으로 ArrayList에 몇번째인지 파악한다.
                int tagNumber = Integer.parseInt(clickTagNumber); // String형태인 Tag값을 Index값을 지정해주기 위해서 사용

                if (checkingTag == 1){
                    Intent it2 = new Intent(ParkingLotList.this,SearchResult.class);
                    it2.putExtra("tag",4); // 4로 고정시킨 이유는 1 2 3 은 이미 다른 액티비티에서 넘어올때 구분값으로 쓰기때문
                    it2.putExtra("parkingName",dynamicParkingName); // 주차장 이름
                    it2.putExtra("currentAddress",currentAddress); // 현재위치
                    it2.putExtra("latitude", parkingArray.get(tagNumber).getLatitude()); // 해당 주차장에 위도값을 넘김
                    it2.putExtra("longitude",parkingArray.get(tagNumber).getLongitude()); // 해당 주차장에 경도값을 넘김
                    startActivity(it2);
                }else if (checkingTag == 2){
                    Intent it2 = new Intent(ParkingLotList.this,SearchResult.class);
                    it2.putExtra("tag",4); // 4로 고정시킨 이유는 1 2 3 은 이미 다른 액티비티에서 넘어올때 구분값으로 쓰기때문

                    it2.putExtra("currentAddress",currentAddress); // 현재위치
                    it2.putExtra("latitude", currentLatitude); // 해당 주차장에 위도값을 넘김
                    it2.putExtra("longitude", currentLongitude); // 해당 주차장에 경도값을 넘김
                    it2.putExtra("parkingName",dynamicParkingName); // 주차장 이름
                    it2.putExtra("destinationAddress",destinationAddress); // 도착지 위치
                    it2.putExtra("latitude2", parkingArray.get(tagNumber).getLatitude()); // 해당 주차장에 위도값을 넘김
                    it2.putExtra("longitude2",parkingArray.get(tagNumber).getLongitude()); // 해당 주차장에 경도값을 넘김
                    startActivity(it2);
                }


            }
        });

        return navigation;
    }



    // runnable은 Thread에 들어갈 실행내용이다(?)
    // 메인에서 이러한 UI동작을 하면 느리고 나중에 동적으로 처리해야할 부분이기 때문에 쓰레드로 처리하는 것
    // 하지만 UI관련은 무조건 메인쓰레드에서 돌려야한다 그래서 서브쓰레드에서도 돌아갈 수 있게하기 위해서는
    // Runnable 객체에 쓰레드에 돌아갈 실행내용을 적고 그 내용이 runnable 객체에 들어가있고 runOnUiThread에 넘긴다.

    final Runnable runnable = new Runnable() {

         /*
            함수명   : run()
            간략     : 레이아웃을 동적생성시킨다.
            상세     : runOnUIThread()에서 돌아갈 주차장정보를 받아와서 동적으로 생성시킬 쓰레드 내용을 적는다.
            작성자   : 이성재
            날짜     : 2021.06.05
            why      : 2km내에 주차장 정보가 들어가있는 ArrayList갯수만큼 동적레이아웃을 생성시키기위해 사용했다 그리고 UI관련 쓰레드는 메인에서 돌려야하기 때문에
                       주차장 정보 받아오고 UI를 나중에 생성시켜야할 경우 이렇게 해야하기 때문에 사용했다.
        */

        @Override
        public void run() {
            {
                while(iter < parkingArray.size()){
                    LinearLayout parkingList = (LinearLayout)findViewById(R.id.parkingList); // ParkingList에 정보를 받아옴 parkingListInner를 그 안에 넣기 위해

                    LinearLayout parkingListOutside = makeDynamicOutsideLinearLayout(); // 모든 걸 감싸는 큰 레이아웃 생성
                    LinearLayout parkingListInner = makeDynamicOutsideLinearLayout2(); //  리니어 레이아웃과 길찾기 아이콘이 들어갈 리니어 레이아웃
                    LinearLayout parkingListInnerInner = makeDynamicInsideLinearLayout(); // 주차장이미지랑 주차장이름이 들어갈 레이아웃
                    ImageView parkingImage = makeDynamicParkinglotImage(); // 주차장 이미지

                    int parkingImgWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,400,getResources().getDisplayMetrics());
                    int parkingImgHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics());
                    parkingImage.getLayoutParams().height = parkingImgHeight;
                    parkingImage.getLayoutParams().width = parkingImgWidth;

                    TextView parkingName = makeDynamicTxt(); // 주차장 정보 (이름, 거리)
                    parkingName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

                    ImageView navigation = makeDynamicNavigationImage(parkingListInnerInner); // 길찾기로 이동하는 아이콘
                    int navigationImgWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,getResources().getDisplayMetrics());
                    int navigationImgHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,getResources().getDisplayMetrics());
                    navigation.getLayoutParams().width = navigationImgWidth;
                    navigation.getLayoutParams().height = navigationImgHeight;

                    parkingList.addView(parkingListOutside); // parkingList 레이아웃 안에 parkingListOutside를 넣음(모든 걸 감싸는 리니어 레이아웃)
                    parkingListOutside.addView(parkingListInner); // 모든 주차장 정보가 들어가는 레이아웃 생성
                    parkingListInner.addView(parkingListInnerInner); //  주차장 이미지랑 이름이 들어가는 부분
                    parkingListInner.addView(navigation); // 길찾기 아이콘
                    parkingListInnerInner.addView(parkingImage); //  주차장 사진
                    parkingListInnerInner.addView(parkingName); // 주차장 이름

                    iter++;
                }

            }
        }
    };

     /*
        함수명   : connectHttp
        간략     : 공공데이터와 Http 통신을 연결한다.
        상세     : 공공데이터의 주차장 정보를 받기 위한 Http 연결
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : pagesNo : 공공데이터의 주차장 정보 페이지 번호
        return   : conn : http 통신정보를 반환한다.
        why      : 공공데이터의 주차장정보가 들어간 페이지와 Http 통신을하기 위해 만들었습니다.
    */

    public HttpURLConnection connectHttp(int pagesNo){

        HttpURLConnection conn = null;
        if (conn == null){
            StringBuilder urlBuilder = null;
            urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_prkplce_info_api"); // 공공데이터 URL

            try {
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=JrZ6ykYHDilgfV9JOmu1eMgNO1%2BkLB0mjCSJSmy%2Fm%2BkS3D2kzPKHJxy0Q4%2B%2FGkxX8S2U3F4HUaPU7d6uoqqe6w%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(pagesNo), "UTF-8"));  // 페이지 번호
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("200", "UTF-8")); // 한 페이지 결과 수
//                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("50", "UTF-8")); // 한 페이지 결과 수
                urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); // XML/JSON 여부
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            URL url = null;
            try {
                url = new URL(urlBuilder.toString()); // StringBuilder를 string형으로 변환
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                conn = (HttpURLConnection) url.openConnection(); // url에 연결함
                conn.setRequestMethod("GET"); // get방식으로 요청 json때문에 쿼리스트링으로 찾아야하니까
                conn.setRequestProperty("Content-type", "application/json"); // json 형식으로 요청
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return conn;
    }

    /*
       함수명   : saveLines
       간략     : 공공데이터 정보를 저장합니다.
       상세     : BufferReader를 생성해 연결한 공공데이터 정보를 읽고 StringBuilder에 저장한다.
       작성자   : 이성재
       날짜     : 2021.06.06
       param    : rd : 공공데이터를 읽어오기위한 BufferedReader 입니다.
       return   : sb : 읽어온 내용을 저장한 것을 반환합니다.
       why      : Http 방식으로 연결한 페이지의 정보를 저장하기 위해 만들었습니다.
   */

    public StringBuilder saveLines(BufferedReader rd){
        StringBuilder sb = new StringBuilder();
        String line = null;

        while (true) {
            try {
                if ((line = rd.readLine()) != null) { // rd에 line을 읽는다. json에서는 한줄로 되어있기 때문에
                    sb.append(line);
                } else {
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){

            }
        }

        return sb;
    }



     /*
        함수명   : readHttp
        간략     : BufferReader를 생성한다
        상세     : 공공데이터 정보를 읽어오기 위한 BufferReader를 생성한다
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : conn : http 통신으로 공공데이터 주차장과 연결한 Connection
        return   : rd : 읽어오기 위해 필요한 BufferedReader
        why      : Http 방식으로 연결한 페이지의 정보를 저장하기 위해 만들었습니다.
     */

    public BufferedReader readHttp(HttpURLConnection conn){
        BufferedReader rd = null;
        try {
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { // BufferedWriter : System.out.println()과 유사
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); // BufferReader : Scanner와 유사
                // getInputStreamReader : byte를 읽어서, decode를 한다고 되어 있는데, 명시된 charset을 이용한다
                // getInputStream : message-body로 넘어온 parameter 확인을 위해서
                // BufferedReader를 최고의 효율로 사용하기 위해 InputStreamReader를 포함
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream())); // 에러 발생 시에 리턴되는 데이터는 getErrorStream() 메서드를 통해 얻을 수 있다
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            // 다시 재실행
        } catch (NoSuchElementException e){
            // 다시 재실행
        }

        /*
            둘은 모두 기존에 쓰던 scanner와 System.out.println()보다 속도 측면에서 훨씬 빠르기 때문에
            (입력된 데이터가 바로 전달되지 않고 버퍼를 거쳐 전달되므로 데이터 처리 효율성을 높임)
            많은 양의 데이터를 처리할 때 유리하다.
            하지만 그에 비해 BufferdReader은 Enter만 경계로 인식하고 받은 데이터사 String으로 고정되기 때문에 입력받은 데이터를 가공하는 작업이 필요한 경우가 많다.
        */

        return rd;


    }

     /*
        함수명   : parseHttpData
        간략     : 저장한 데이터의 목록 (json)에서 필요한 정보를 파싱해서 저장한다.
        상세     : json형식으로 저장된 주차장 정보중에서 필요한 정보만 골라서 파싱한다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : sb : 공공데이터에서 받아온 정보들입니다.
        why      : 공공데이터에서 필요한 정보만 파싱하기 위해서 만들었습니다.
     */

    public void parseHttpData(StringBuilder sb){
        try {
            String sbString = sb.toString();
            JSONObject jsonObject = new JSONObject(sbString); // 받아온 line(json형태)를 객체로 생성해 담음

            String response = jsonObject.getString("response"); // response 키와 값을 문자열로 가져온다 {header :{...
            JSONObject jsonResponse = new JSONObject(response); // response라는 문자열을 json객체로 만듬(파싱하기 위해 사용)

            String checkingPageNo = jsonResponse.getString("header");
            JSONObject jsonHeader = new JSONObject(checkingPageNo);
            resultCode = jsonHeader.getString("resultCode"); // 데이터가 없는 페이지의 경우 resultCode값이 03이기 때문에
            String body = jsonResponse.getString("body"); // body에 해당하는 키를 가져옴
            JSONObject jsonBody = new JSONObject(body); // body에 해당되는 값을 가져옴

            String informations = jsonBody.getString("items"); // items에 키를 가져옴

            JSONArray jsonArray = new JSONArray(informations); //items:[ 로 시작하면 [가 배열이기 때문에 jsonArray를 만들어야한다.


            // getString (A) : A에 해당하는 값을 가져오기
            // JSONObject (A) : A 문자열에 해당하는 json형태를 가져오기 {} 형태
            // JSONArray (A) : 값이 배열로 되어있는 거에 들어가려면 for문 하고 같이 써야한다


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i); // 배열 안에 각 인덱스를 받음 item : [안에 있는 또다른 json

                String latitude = subJsonObject.getString("latitude"); // 주차장 위도
                String longitude = subJsonObject.getString("longitude"); // 주차장 경도


                try {
                    boolean getterSwi = findNearbyParkingLot(latitude, longitude);
                    if (getterSwi == false) {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }


                String prkplceNm = subJsonObject.getString("prkplceNm");  // 주차장명
                String prkplceSe = subJsonObject.getString("prkplceSe"); // 주차장 구분
                String prkplceType = subJsonObject.getString("prkplceType"); // 주차장유형
                String rdnmadr = subJsonObject.getString("rdnmadr"); // 주소
                String prkcmprt = subJsonObject.getString("prkcmprt");// 구획수
                String feedingSe = subJsonObject.getString("feedingSe"); // 급지
                String enforceSe = subJsonObject.getString("enforceSe");// 부제시행
                String operDay = subJsonObject.getString("operDay"); // 운영요일
                String weekdayOperOpenHhmm = subJsonObject.getString("weekdayOperOpenHhmm"); // 평일 운영시작
                String weekdayOperColseHhmm = subJsonObject.getString("weekdayOperColseHhmm");// 평일 운영 종료
                String satOperOperOpenHhmm = subJsonObject.getString("satOperOperOpenHhmm"); // 토요일 운영 시작
                String satOperCloseHhmm = subJsonObject.getString("satOperCloseHhmm");  //  토요일 운영 종료
                String holidayOperOpenHhmm = subJsonObject.getString("holidayOperOpenHhmm"); //  공휴일 운영시작
                String holidayCloseOpenHhmm = subJsonObject.getString("holidayCloseOpenHhmm"); // 공휴일 운영종료
                String parkingchrgeInfo = subJsonObject.getString("parkingchrgeInfo"); // 요금정보
                String basicTime = subJsonObject.getString("basicTime"); //  주차 기본시간
                String basicCharge = subJsonObject.getString("basicCharge"); // 주차 기본요금
                String addUnitTime = subJsonObject.getString("addUnitTime"); // 추가 단위 시간
                String addUnitCharge = subJsonObject.getString("addUnitCharge"); // 추가 단위 요금
                String dayCmmtktAdjTime = subJsonObject.getString("dayCmmtktAdjTime"); // 1일주차권요금적용시간
                String dayCmmtkt = subJsonObject.getString("dayCmmtkt"); // 1일주차권요금
                String monthCmmtkt = subJsonObject.getString("monthCmmtkt"); // 월정기권요금
                String metpay = subJsonObject.getString("metpay"); // 결제방법
                String spcmnt = subJsonObject.getString("spcmnt"); // 특기사항
                String institutionNm = subJsonObject.getString("institutionNm"); // 관리기관명
                String phoneNumber = subJsonObject.getString("phoneNumber"); // 전화번호
                String latitude2 = subJsonObject.getString("latitude"); // 위도
                String longitude2 = subJsonObject.getString("longitude"); // 경도

                NearbyParkingLotSetterGetter tempParkingLot = new NearbyParkingLotSetterGetter(prkplceNm, prkplceSe, prkplceType, rdnmadr, prkcmprt, feedingSe, enforceSe, operDay, weekdayOperOpenHhmm, weekdayOperColseHhmm, satOperOperOpenHhmm, satOperCloseHhmm, holidayOperOpenHhmm, holidayCloseOpenHhmm, parkingchrgeInfo, basicTime, basicCharge, addUnitTime, addUnitCharge, dayCmmtktAdjTime, dayCmmtkt, monthCmmtkt, metpay, spcmnt, institutionNm, phoneNumber, latitude2,longitude2);
                parkingArray.add(tempParkingLot);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

     /*
        함수명   : closeBuffer
        간략     : Buffer 생성한 걸 닫습니다.
        상세     : Http 통신해서 공공데이터를 받아오기 위해 생성한 Buffer를 닫습니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        why      : Buffer를 닫기 위해 만들었습니다.
     */

    public void closeBuffer(BufferedReader rd){
        try {
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

     /*
        함수명   : disconnectHttp
        간략     : Http 통신을 끊습니다.
        상세     : 공공데이터를 받기 위해 Http 통신한 걸 끊습니다.
        작성자   : 이성재
        날짜     : 2021.06.06
        param    : conn : 공공데이터와 http 통신한 객체
        why      : Http 통신을 끊기 위해 만들었습니다.
     */

    public void disconnectHttp(HttpURLConnection conn){
        conn.disconnect();
    }

         /*
           클래스명 : MakingViewsAsync
           간략     : 받아온 주차장 정보를 동적으로 뷰들을 생성하기 위한 비동기식 방식
           상세     : 주차장 정보가 뒤늦게 받아오고 그후 UI처리를 해야하기 때문에 비동기식 방식을 사용함
           작성자   : 이성재
           날짜     : 2021.06.05
         */

    class MakingViewsAsync extends AsyncTask<Void,Void,Void>{

        /*
            함수명   : doInBackground()
            간략     : 주차장정보 UI를 처리할 쓰레드를 돌리는 비동기식방식
            상세     : Runnable 객체에 선언한 쓰레드 내용을 NewRunnable에서 그 내용을 UI메인 쓰레드로 돌린다고 하는 내용을 쓰레드로 돌린다.
            작성자   : 이성재
            날짜     : 2021.06.06
            why      : 주차장 정보를 가져오고나서 UI처리를 해야하기 때문에 ParkingListAsync라는 주차장 정보를 받아오는 비동기방식이 끝난 후  onPostExecute에 사용하기 위해 만들었다.
        */

        @Override
        protected Void doInBackground(Void... voids) {


            // 배열들의 거리를 정렬해줄 메소드

            NewRunnable nr = new NewRunnable();
            // NewRunnable 객체를 생성
            Thread t = new Thread(nr) ;
            // runnable을 Thread로 돌려야한다.
            t.start() ;
            // Thread 실행
            return null;
        }
    }

         /*
           클래스명 : ParkingListAsync
           간략     : 주차장정보를 비동기식방식으로 가져옵니다.
           상세     : 주차장정보를 비동기식방식으로 처리후 동적뷰를 생성하는 비동기식방식을 실행합니다.
           작성자   : 이성재
           날짜     : 2021.06.05
         */

    class ParkingListAsync extends AsyncTask<Integer,Void,Void>{

        /*
            함수명   : onPostExecute()
            간략     : 완료되면 프로그레스바를 지우고 동적으로 레이아웃을 생성하고 값을 넣습니다.
            상세     : 완료되면 프로그레스바를 지우고 동적으로 주차장정보가 들어갈 뷰들을 생성하고 값을 넣습니다.
            작성자   : 이성재
            날짜     : 2021.06.06
            why      : 함수는 한가지 일만 해야한다고 하지만 아무리 생각해도 분리할 방법을 몰라서 2가지 기능을 넣었습니다.
        */

        @Override
        protected void onPostExecute(Void avoid) {

                super.onPostExecute(avoid);
                mProgress.setVisibility(View.GONE);
                new MakingViewsAsync().execute();
        }


        /*
            함수명   : doInBackground()
            간략     : 공공데이터를 파싱합니다.
            상세     : 주차장 정보 공공데이터를 파싱합니다.
            작성자   : 이성재
            날짜     : 2021.06.06
            why      : 주차장 정보 공공데이터를 파싱하기 위해 만들었습니다.
        */

        @Override
        protected Void doInBackground(Integer... pagesNo) { // String 데이터 변경을 위해 StringBuilder로 변경한 다음에 append, insert 등의 기능을 처리

            HttpURLConnection conn = connectHttp(pagesNo[0]);
            BufferedReader rd = readHttp(conn);
            StringBuilder sb = saveLines(rd);
            parseHttpData(sb);
            closeBuffer(rd);
            disconnectHttp(conn);

            Log.e("모든 조회", "끝낫습니다." + parkingArray.size());
            return null;
        }
    }

}