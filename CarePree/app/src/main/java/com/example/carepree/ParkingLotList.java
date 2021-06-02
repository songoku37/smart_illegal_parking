package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParkingLotList extends AppCompatActivity {

    final double PER_LATITUDE = 0.0090909090909091; // 1km당 위도
    final double PER_LONGITUDE = 0.0112688753662384; // 1km당 경도
    String resultCode = "";
    int iter = 0;
    List<NearbyParkingLotSetterGetter> parkingArray = new ArrayList<NearbyParkingLotSetterGetter>(100);
    double currentLatitude = 0;
    double currentLongitude = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_list);

//        Intent it = getIntent();
//        currentLongitude= it.getDoubleExtra("currentLatitude",0);
//        currentLongitude = it.getDoubleExtra("currentLongitude",0);
        currentLatitude = 36.785914;
        currentLongitude = 127.5838292988;

       // double twoKmLatitude = PER_LATITUDE * 2; // 2km 범위내 위도
       // double twoKmLongitude = PER_LONGITUDE * 2; // 2km 범위내 경도


        ExecutorService THREAD_POOL = Executors.newFixedThreadPool(33);

        for (int i = 0 ; i <= 32 ; i++){
            new ParkingListAsync().executeOnExecutor(THREAD_POOL, i);
        }



    }

    public void enterParkingLotInformation(View v){ // 주차정보화면으로 이동
        Intent it = new Intent(this,ParkingLotInformation.class);
        startActivity(it);
    }

    public void enterFilteringFromParkingLotList(View v){ // 주차장 화면 리스트로 이동
        Intent it = new Intent(this,FilteringFromParkingLotList.class);
        startActivity(it);
    }

    public void enterSearchResult(View v){ // 길찾기 화면으로 이동
        Intent it = new Intent(this,SearchResult.class);
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

    public boolean findNearbyParkingLot(String latitude,String longitude){ // 2km 범위내에 있는지 확인하기 위한 메소드

        double dLatitude = Double.parseDouble(latitude);
        double dLongitude = Double.parseDouble(longitude);
        boolean judgeSwi = false;

        if((dLatitude < (currentLatitude + (PER_LATITUDE * 2))) && dLatitude > ((currentLatitude) - (PER_LATITUDE * 2))){ // 2km 위도 범위내에 있으면
            if((dLongitude < (currentLongitude) + (PER_LATITUDE * 2)) && dLongitude > ((currentLongitude) - (PER_LONGITUDE * 2))){ // 2km 경도 범위내에 있으면
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

    class NewRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 메인 스레드에 runnable 전달.
                runOnUiThread(runnable) ;
        }
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while(iter < parkingArray.size()){


                String dyanmicParkingName = parkingArray.get(iter).getPrkplceNm();

                LinearLayout parkingList = (LinearLayout)findViewById(R.id.parkingList); // ParkingList에 정보를 받아옴 parkingListInner를 그 안에 넣기 위해

                LinearLayout parkingListInner = new LinearLayout(ParkingLotList.this); // ParkingLotList라는 xml에 dynamicLayout이라는 이름으로 만듦
                parkingListInner.setOrientation(LinearLayout.HORIZONTAL); // Orientation을 수직으로
                parkingListInner.setGravity(Gravity.CENTER); // 가운데 정렬
                LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT); // 너비 높이 조정
                parkingListInner.setLayoutParams(params0); // 적용


                // 주차장이미지랑 주차장이름이 들어갈 레이아웃
                LinearLayout parkingListInnerInner = new LinearLayout(ParkingLotList.this);
                parkingListInnerInner.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1200 ,LinearLayout.LayoutParams.WRAP_CONTENT);
                parkingListInnerInner.setGravity(Gravity.CENTER); // 가운데 정렬
                parkingListInnerInner.setLayoutParams(params);


                // 주차장 이미지
                ImageView parkingImage = new ImageView(ParkingLotList.this);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(400,250);
                parkingImage.setImageResource(R.drawable.parkinglotimg);
                parkingImage.setLayoutParams(params2);

                // 주차장 정보 (이름, 거리)
                TextView parkingName = new TextView(ParkingLotList.this); // width와 height 설정
                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                parkingName.setLayoutParams(params3);
                parkingName.setText(dyanmicParkingName);

                // 길찾기로 이동하는 아이콘
                ImageView navigation = new ImageView(ParkingLotList.this);
                LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(120,150);
                params4.bottomMargin = 30; // 정렬이 조금 애매해서 주었음
                navigation.setLayoutParams(params4);
                navigation.setImageResource(R.drawable.ic_getdirections);


                parkingList.addView(parkingListInner); // parkingList 레이아웃 안에 parkingListInner를 넣음
                parkingListInner.addView(parkingListInnerInner); //  주차장 이미지랑 이름이 들어가는 부분
                parkingListInnerInner.addView(parkingImage); //  주차장 사진
                parkingListInnerInner.addView(parkingName); // 주차장 이름
                parkingListInner.addView(navigation); // 길찾기 아이콘

                iter++;
            }
        }
    };






    class FindNearbyParkingLotAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {


            // 배열들의 거리를 정렬해줄 메소드
            NewRunnable nr = new NewRunnable() ;
            Thread t = new Thread(nr) ;
            t.start() ;

            return null;
        }
    }

    class ParkingListAsync extends AsyncTask<Integer,Void,Integer>{


        @Override
        protected void onPostExecute(Integer lastNo) {
            super.onPostExecute(lastNo);
            if(lastNo == 32){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                {
                new FindNearbyParkingLotAsync().execute();
            }}
        }



        @Override
        protected Integer doInBackground(Integer... pagesNo) { // String 데이터 변경을 위해 StringBuilder로 변경한 다음에 append, insert 등의 기능을 처리

            StringBuilder urlBuilder = null;
            urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_prkplce_info_api"); // 공공데이터 URL

            try {
                Log.e("resultCode", " 시작 " + pagesNo[0]);

                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=JrZ6ykYHDilgfV9JOmu1eMgNO1%2BkLB0mjCSJSmy%2Fm%2BkS3D2kzPKHJxy0Q4%2B%2FGkxX8S2U3F4HUaPU7d6uoqqe6w%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(pagesNo[0]), "UTF-8"));  // 페이지 번호
//                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("30", "UTF-8"));  // 페이지 번호
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("400", "UTF-8")); // 한 페이지 결과 수
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
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection(); // url에 연결함
                conn.setRequestMethod("GET"); // get방식으로 요청 json때문에 쿼리스트링으로 찾아야하니까
                conn.setRequestProperty("Content-type", "application/json"); // json 형식으로 요청
            } catch (IOException e) {
                e.printStackTrace();
            }



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
            }

            /*
            둘은 모두 기존에 쓰던 scanner와 System.out.println()보다 속도 측면에서 훨씬 빠르기 때문에
            (입력된 데이터가 바로 전달되지 않고 버퍼를 거쳐 전달되므로 데이터 처리 효율성을 높임)
            많은 양의 데이터를 처리할 때 유리하다.
            하지만 그에 비해 BufferdReader은 Enter만 경계로 인식하고 받은 데이터사 String으로 고정되기 때문에 입력받은 데이터를 가공하는 작업이 필요한 경우가 많다.
             */

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
                }
            }


            String sbString = sb.toString();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(sbString); // 받아온 line(json형태)를 객체로 생성해 담음

                String response = jsonObject.getString("response"); // response 키와 값을 문자열로 가져온다 {header :{...
                JSONObject jsonResponse = new JSONObject(response); // response라는 문자열을 json객체로 만듬(파싱하기 위해 사용)

                String checkingPageNo = jsonResponse.getString("header");
                JSONObject jsonHeader = new JSONObject(checkingPageNo);
                resultCode = jsonHeader.getString("resultCode"); // 데이터가 없는 페이지의 경우 resultCode값이 03이기 때문에
                String body = jsonResponse.getString("body"); // body에 해당하는 키를 가져옴
                JSONObject jsonBody = new JSONObject(body); // body에 해당되는 값을 가져옴

                String informations = jsonBody.getString("items"); // items에 키를 가져옴

                JSONArray jsonArray = new JSONArray(informations); //items:[ 로 시작하면 [가 배열이기 때문에 jsonArray를 만들어야한다.

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

//                        Log.e("성공",i + "번째 성공" );
                    NearbyParkingLotSetterGetter tempParkingLot = new NearbyParkingLotSetterGetter(prkplceNm, prkplceSe, prkplceType, rdnmadr, prkcmprt, feedingSe, enforceSe, operDay, weekdayOperOpenHhmm, weekdayOperColseHhmm, satOperOperOpenHhmm, satOperCloseHhmm, holidayOperOpenHhmm, holidayCloseOpenHhmm, parkingchrgeInfo, basicTime, basicCharge, addUnitTime, addUnitCharge, dayCmmtktAdjTime, dayCmmtkt, monthCmmtkt, metpay, spcmnt, institutionNm, phoneNumber);
                    parkingArray.add(tempParkingLot);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.disconnect();

            // getString (A) : A에 해당하는 값을 가져오기
            // JSONObject (A) : A 문자열에 해당하는 json형태를 가져오기 {} 형태
            // JSONArray (A) : 값이 배열로 되어있는 거에 들어가려면 for문 하고 같이 써야한다


            Log.e("모든 조회", "끝낫습니다." + parkingArray.size());

            return pagesNo[0];
        }
    }
}