package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_information);
        Intent it = getIntent();
        NearbyParkingLotSetterGetter informationData = (NearbyParkingLotSetterGetter) it.getSerializableExtra("parkingInformationsArrays");


        LinearLayout parkingInfoList = (LinearLayout)findViewById(R.id.information); // 주차장 정보가 들어갈 레이아웃

        List<LinearLayout> parkingInfoLayoutArrays = new ArrayList<LinearLayout>(); // 배열에다가 밑에 적은 LinearLayout설정한 걸 넣음

        List<TextView> parkingInfoArrays = new ArrayList<TextView>(); // 배열에다가 밑에 적은 TextView설정한 걸 넣음

        for (int i = 0 ; i < 26 ; i++ ){
            // 왜 레이아웃과 텍스트뷰를 위에 선언 해서 add만 적용하면 편하지 않냐고 하는데
            // 이게 그렇게되면 A.addView(B)할때 A에 B를 넣는 것인데 그 다음 A.addView(C)하면 에러난다
            // 왜냐하면 A는 이미 B라는 자식을 가지고 있기 때문에 C를 가질 수 없다
            // 그래서 밑에 처럼 계속 생성 안 해주고 전역으로 생성한 걸 쓰면 똑같은 위치에 있는 걸 계속 참조하기 때문에
            // LinearLayout에 설정과 TextView에 설정이 26개가 들어가도 다 완전 동일한 걸로 취급해서
            // 위와 같이 자식을 1개밖에 가질 수 없기 때문에 참조도 제대로 안 대고 addView하면 에러가 터진다.

            LinearLayout parkingInfoLayout = new LinearLayout(this); // 정보가 들어갈 레이아웃 생성
            parkingInfoLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parkingInfoLayout.setLayoutParams(params);


            TextView parkingInfo = new TextView(this); // width와 height 설정
            ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parkingInfo.setLayoutParams(params2);

            parkingInfoLayoutArrays.add(parkingInfoLayout);
            parkingInfoArrays.add(parkingInfo);
        }

        parkingInfoArrays.get(0).setText("주차장 이름       : " + informationData.getPrkplceNm());
        parkingInfoArrays.get(1).setText("주차장 구분 : " + informationData.getPrkplceSe());
        parkingInfoArrays.get(2).setText("주차장 유형 : " + informationData.getPrkplceType());
        parkingInfoArrays.get(3).setText("도로명 주소 : " + informationData.getRdnmadr());
        parkingInfoArrays.get(4).setText("주차 구획수 : " + informationData.getPrkcmprt());
        parkingInfoArrays.get(5).setText("급지 구분          :" + informationData.getFeedingSe());
        parkingInfoArrays.get(6).setText("부제시행구분       : " + informationData.getEnforceSe());
        parkingInfoArrays.get(7).setText("운영요일           :" + informationData.getOperDay());
        parkingInfoArrays.get(8).setText("평일운영시작시각   : " + informationData.getWeekdayOperOpenHhmm());
        parkingInfoArrays.get(9).setText("평일운영종료시각   : " + informationData.getWeekdayOperColseHhmm());
        parkingInfoArrays.get(10).setText("토요일운영시작시각 : " + informationData.getSatOperOperOpenHhmm());
        parkingInfoArrays.get(11).setText("토요일운영종료시각 : " + informationData.getSatOperCloseHhmm());
        parkingInfoArrays.get(12).setText("공휴일운영시작시각 : " + informationData.getHolidayOperOpenHhmm());
        parkingInfoArrays.get(13).setText("공휴일운영종료시각 : " + informationData.getHolidayCloseOpenHhmm());
        parkingInfoArrays.get(14).setText("요금정보           : " + informationData.getParkingchrgeInfo());
        parkingInfoArrays.get(15).setText("주차기본시간         : " + informationData.getBasicTime());
        parkingInfoArrays.get(16).setText("주차기본요금 : " + informationData.getBasicCharge());
        parkingInfoArrays.get(17).setText("추가단위시간 : " + informationData.getAddUnitTime());
        parkingInfoArrays.get(18).setText("추가단위요금 : " + informationData.getAddUnitCharge());
        parkingInfoArrays.get(19).setText("1일주차권요금적용시간 : " + informationData.getDayCmmtktAdjTime());
        parkingInfoArrays.get(20).setText("1일주차권요금 : " + informationData.getDayCmmtkt());
        parkingInfoArrays.get(21).setText("월정기권요금 : " + informationData.getMonthCmmtkt());
        parkingInfoArrays.get(22).setText("결제방법 : " + informationData.getMetpay());
        parkingInfoArrays.get(23).setText("특기사항 : " + informationData.getSpcmnt());
        parkingInfoArrays.get(24).setText("관리기관명 : " + informationData.getInstitutionNm());
        parkingInfoArrays.get(25).setText("전화번호 : " + informationData.getPhoneNumber());


        for(int i = 0 ; i < parkingInfoLayoutArrays.size(); i++){
            parkingInfoLayoutArrays.get(i).addView(parkingInfoArrays.get(i));
            parkingInfoList.addView(parkingInfoLayoutArrays.get(i));
        }




    }

    public void enterFavorites(View v){ // 즐겨찾기로 이동
        Intent it = new Intent(this,Favorites.class);
        startActivity(it);
    }

    public void enterMain(View v){ // 메인 화면으로 이동
        Intent it = new Intent(this,NearParkingLotMain.class);
        startActivity(it);
    }

    public void enterSettings(View v){ // 설정으로 이동
        Intent it = new Intent(this,Settings.class);
        startActivity(it);
    }
}