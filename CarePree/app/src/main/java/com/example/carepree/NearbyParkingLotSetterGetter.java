package com.example.carepree;

public class NearbyParkingLotSetterGetter {

    String prkplceNm;  // 주차장명
    String prkplceSe; // 주차장 구분
    String prkplceType; // 주차장유형
    String rdnmadr; // 주소
    String prkcmprt;// 구획수
    String feedingSe; // 급지
    String enforceSe;// 부제시행

    public String getPrkplceNm() {
        return prkplceNm;
    }

    public void setPrkplceNm(String prkplceNm) {
        this.prkplceNm = prkplceNm;
    }

    public String getPrkplceSe() {
        return prkplceSe;
    }

    public void setPrkplceSe(String prkplceSe) {
        this.prkplceSe = prkplceSe;
    }

    public String getPrkplceType() {
        return prkplceType;
    }

    public void setPrkplceType(String prkplceType) {
        this.prkplceType = prkplceType;
    }

    public String getRdnmadr() {
        return rdnmadr;
    }

    public void setRdnmadr(String rdnmadr) {
        this.rdnmadr = rdnmadr;
    }

    public String getPrkcmprt() {
        return prkcmprt;
    }

    public void setPrkcmprt(String prkcmprt) {
        this.prkcmprt = prkcmprt;
    }

    public String getFeedingSe() {
        return feedingSe;
    }

    public void setFeedingSe(String feedingSe) {
        this.feedingSe = feedingSe;
    }

    public String getEnforceSe() {
        return enforceSe;
    }

    public void setEnforceSe(String enforceSe) {
        this.enforceSe = enforceSe;
    }

    public String getOperDay() {
        return operDay;
    }

    public void setOperDay(String operDay) {
        this.operDay = operDay;
    }

    public String getWeekdayOperOpenHhmm() {
        return weekdayOperOpenHhmm;
    }

    public void setWeekdayOperOpenHhmm(String weekdayOperOpenHhmm) {
        this.weekdayOperOpenHhmm = weekdayOperOpenHhmm;
    }

    public String getWeekdayOperColseHhmm() {
        return weekdayOperColseHhmm;
    }

    public void setWeekdayOperColseHhmm(String weekdayOperColseHhmm) {
        this.weekdayOperColseHhmm = weekdayOperColseHhmm;
    }

    public String getSatOperOperOpenHhmm() {
        return satOperOperOpenHhmm;
    }

    public void setSatOperOperOpenHhmm(String satOperOperOpenHhmm) {
        this.satOperOperOpenHhmm = satOperOperOpenHhmm;
    }

    public String getSatOperCloseHhmm() {
        return satOperCloseHhmm;
    }

    public void setSatOperCloseHhmm(String satOperCloseHhmm) {
        this.satOperCloseHhmm = satOperCloseHhmm;
    }

    public String getHolidayOperOpenHhmm() {
        return holidayOperOpenHhmm;
    }

    public void setHolidayOperOpenHhmm(String holidayOperOpenHhmm) {
        this.holidayOperOpenHhmm = holidayOperOpenHhmm;
    }

    public String getHolidayCloseOpenHhmm() {
        return holidayCloseOpenHhmm;
    }

    public void setHolidayCloseOpenHhmm(String holidayCloseOpenHhmm) {
        this.holidayCloseOpenHhmm = holidayCloseOpenHhmm;
    }

    public String getParkingchrgeInfo() {
        return parkingchrgeInfo;
    }

    public void setParkingchrgeInfo(String parkingchrgeInfo) {
        this.parkingchrgeInfo = parkingchrgeInfo;
    }

    public String getBasicTime() {
        return basicTime;
    }

    public void setBasicTime(String basicTime) {
        this.basicTime = basicTime;
    }

    public String getBasicCharge() {
        return basicCharge;
    }

    public void setBasicCharge(String basicCharge) {
        this.basicCharge = basicCharge;
    }

    public String getAddUnitTime() {
        return addUnitTime;
    }

    public void setAddUnitTime(String addUnitTime) {
        this.addUnitTime = addUnitTime;
    }

    public String getAddUnitCharge() {
        return addUnitCharge;
    }

    public void setAddUnitCharge(String addUnitCharge) {
        this.addUnitCharge = addUnitCharge;
    }

    public String getDayCmmtktAdjTime() {
        return dayCmmtktAdjTime;
    }

    public void setDayCmmtktAdjTime(String dayCmmtktAdjTime) {
        this.dayCmmtktAdjTime = dayCmmtktAdjTime;
    }

    public String getDayCmmtkt() {
        return dayCmmtkt;
    }

    public void setDayCmmtkt(String dayCmmtkt) {
        this.dayCmmtkt = dayCmmtkt;
    }

    public String getMonthCmmtkt() {
        return monthCmmtkt;
    }

    public void setMonthCmmtkt(String monthCmmtkt) {
        this.monthCmmtkt = monthCmmtkt;
    }

    public String getMetpay() {
        return metpay;
    }

    public void setMetpay(String metpay) {
        this.metpay = metpay;
    }

    public String getSpcmnt() {
        return spcmnt;
    }

    public void setSpcmnt(String spcmnt) {
        this.spcmnt = spcmnt;
    }

    public String getInstitutionNm() {
        return institutionNm;
    }

    public void setInstitutionNm(String institutionNm) {
        this.institutionNm = institutionNm;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String operDay; // 운영요일
    String weekdayOperOpenHhmm; // 평일 운영시작
    String weekdayOperColseHhmm;// 평일 운영 종료
    String satOperOperOpenHhmm; // 토요일 운영 시작
    String satOperCloseHhmm;  //  토요일 운영 종료
    String holidayOperOpenHhmm; //  공휴일 운영시작
    String holidayCloseOpenHhmm; // 공휴일 운영종료
    String parkingchrgeInfo; // 요금정보
    String basicTime; //  주차 기본시간
    String basicCharge; // 주차 기본요금
    String addUnitTime; // 추가 단위 시간
    String addUnitCharge; // 추가 단위 요금
    String dayCmmtktAdjTime; // 1일주차권요금적용시간
    String dayCmmtkt; // 1일주차권요금
    String monthCmmtkt; // 월정기권요금
    String metpay; // 결제방법
    String spcmnt; // 특기사항
    String institutionNm; // 관리기관명
    String phoneNumber; // 전화번호

    public NearbyParkingLotSetterGetter(String prkplceNm, String prkplceSe, String prkplceType, String rdnmadr, String prkcmprt, String feedingSe, String enforceSe, String operDay, String weekdayOperOpenHhmm, String weekdayOperColseHhmm, String satOperOperOpenHhmm, String satOperCloseHhmm, String holidayOperOpenHhmm, String holidayCloseOpenHhmm, String parkingchrgeInfo, String basicTime, String basicCharge, String addUnitTime, String addUnitCharge, String dayCmmtktAdjTime, String dayCmmtkt, String monthCmmtkt, String metpay, String spcmnt, String institutionNm, String phoneNumber) {
        this.prkplceNm = prkplceNm;
        this.prkplceSe = prkplceSe;
        this.prkplceType = prkplceType;
        this.rdnmadr = rdnmadr;
        this.prkcmprt = prkcmprt;
        this.feedingSe = feedingSe;
        this.enforceSe = enforceSe;
        this.operDay = operDay;
        this.weekdayOperOpenHhmm = weekdayOperOpenHhmm;
        this.weekdayOperColseHhmm = weekdayOperColseHhmm;
        this.satOperOperOpenHhmm = satOperOperOpenHhmm;
        this.satOperCloseHhmm = satOperCloseHhmm;
        this.holidayOperOpenHhmm = holidayOperOpenHhmm;
        this.holidayCloseOpenHhmm = holidayCloseOpenHhmm;
        this.parkingchrgeInfo = parkingchrgeInfo;
        this.basicTime = basicTime;
        this.basicCharge = basicCharge;
        this.addUnitTime = addUnitTime;
        this.addUnitCharge = addUnitCharge;
        this.dayCmmtktAdjTime = dayCmmtktAdjTime;
        this.dayCmmtkt = dayCmmtkt;
        this.monthCmmtkt = monthCmmtkt;
        this.metpay = metpay;
        this.spcmnt = spcmnt;
        this.institutionNm = institutionNm;
        this.phoneNumber = phoneNumber;
    }
}