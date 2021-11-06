/*

@ breif  : 주차장 정보 페이지
@ detail : 인근 주차장에 한 목록을 눌렀을 때 그에 대한 주차장 정보를 보여준다.
@ why    : 주차장에 대한 정보를 보고 어떤 주차장을 선택할지 도움을 주기 위해 만들었다.

 */

package com.example.carepree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class ParkingLotInformation extends AppCompatActivity {

    List<LinearLayout> parkingInfoLayoutArrays;
    List<TextView> parkingInfoArrays;
    NearbyParkingLotSetterGetter informationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_information);
        Intent it = getIntent();

        informationData = (NearbyParkingLotSetterGetter) it.getSerializableExtra("parkingInformationsArrays"); // 클릭한 주차장에 해당하는 정보가 있는 객체를 받아옴

        setDynamicTxt(); // 텍스트뷰에 주차장정보를 저장합니다.


    }



      /*
        함수명   : setDynamicTxt
        간략     : 동적 생성한 텍스트뷰에 주차장 정보를 넣습니다.
        상세     : 동적 생성한 텍스트뷰에 주차장 정보를 넣습니다.
        작성자   : 이성재
        날짜     : 2021.06.05
        why      : 반복문으로 받고 싶었지만 setText에 공통점이 거의 없어서 이렇게 길게 만들었습니다.
      */

    public void setDynamicTxt(){

        TextView parkingText1 = (TextView) findViewById(R.id.parkingText1);
        TextView parkingText2 = (TextView) findViewById(R.id.parkingText2);
        TextView parkingText3 = (TextView) findViewById(R.id.parkingText3);
        TextView parkingText4 = (TextView) findViewById(R.id.parkingText4);
        TextView parkingText5 = (TextView) findViewById(R.id.parkingText5);
        TextView parkingText6 = (TextView) findViewById(R.id.parkingText6);
        TextView parkingText7 = (TextView) findViewById(R.id.parkingText7);
        TextView parkingText8 = (TextView) findViewById(R.id.parkingText8);
        TextView parkingText9 = (TextView) findViewById(R.id.parkingText9);
        TextView parkingText10 = (TextView) findViewById(R.id.parkingText10);
        TextView parkingText11 = (TextView) findViewById(R.id.parkingText11);
        TextView parkingText12 = (TextView) findViewById(R.id.parkingText12);
        TextView parkingText13 = (TextView) findViewById(R.id.parkingText13);
        TextView parkingText14 = (TextView) findViewById(R.id.parkingText14);
        TextView parkingText15 = (TextView) findViewById(R.id.parkingText15);
        TextView parkingText16 = (TextView) findViewById(R.id.parkingText16);
        TextView parkingText17 = (TextView) findViewById(R.id.parkingText17);
        TextView parkingText18 = (TextView) findViewById(R.id.parkingText18);
        TextView parkingText19 = (TextView) findViewById(R.id.parkingText19);
        TextView parkingText20 = (TextView) findViewById(R.id.parkingText20);
        TextView parkingText21 = (TextView) findViewById(R.id.parkingText21);
        TextView parkingText22 = (TextView) findViewById(R.id.parkingText22);
        TextView parkingText23 = (TextView) findViewById(R.id.parkingText23);
        TextView parkingText24 = (TextView) findViewById(R.id.parkingText24);
        TextView parkingText25 = (TextView) findViewById(R.id.parkingText25);
        TextView parkingText26 = (TextView) findViewById(R.id.parkingText26);
        parkingText1.setText(informationData.getPrkplceNm());
        parkingText2.setText(informationData.getPrkplceSe());
        parkingText3.setText(informationData.getPrkplceType());
        parkingText4.setText(informationData.getRdnmadr());
        parkingText5.setText(informationData.getPrkcmprt());
        parkingText6.setText(informationData.getFeedingSe());
        parkingText7.setText(informationData.getEnforceSe());
        parkingText8.setText(informationData.getOperDay());
        parkingText9.setText(informationData.getWeekdayOperOpenHhmm());
        parkingText10.setText(informationData.getWeekdayOperColseHhmm());
        parkingText11.setText(informationData.getSatOperOperOpenHhmm());
        parkingText12.setText(informationData.getSatOperCloseHhmm());
        parkingText13.setText(informationData.getHolidayOperOpenHhmm());
        parkingText14.setText(informationData.getHolidayCloseOpenHhmm());
        parkingText15.setText(informationData.getParkingchrgeInfo());
        parkingText16.setText(informationData.getBasicTime());
        parkingText17.setText(informationData.getBasicCharge());
        parkingText18.setText(informationData.getAddUnitTime());
        parkingText19.setText(informationData.getAddUnitCharge());
        parkingText20.setText(informationData.getDayCmmtktAdjTime());
        parkingText21.setText(informationData.getDayCmmtkt());
        parkingText22.setText(informationData.getMonthCmmtkt());
        parkingText23.setText(informationData.getMetpay());
        parkingText24.setText(informationData.getSpcmnt());
        parkingText25.setText(informationData.getInstitutionNm());
        parkingText26.setText(informationData.getPhoneNumber());
    }



     /*
        함수명   : enterMain
        간략     : 메인페이지로 이동
        상세     : 메인페이지로 이동하기 위한 onClick
        작성자   : 이성재
        날짜     : 2021.06.05
        param    : v : 클릭한 View객체
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
}