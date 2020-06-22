package kyungCoupon.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OftenUsedFunction {

    //쿠폰 유효기간 일수 설정값
    //1이면 하루동안 쿠폰이 유효함.
    public static final int VALID_DAY_CNT = 1;


    /*
    입력한 일자에 date에 + addDay한 일자 구하는 함수
    @input String date, int addDay
    @output String date+addDay만큼의 일자
    * */
    public static String getAddedDate(String date, int addDay){
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        try {
            cal.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, addDay);
        return df.format(cal.getTime());
    }


    /* 오늘 일자 구하는 함수
    @input void
    @output String 20200620
    * */
    public static String getTodayDate() {
        long systemTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        return formatter.format(systemTime);
    }

    /* 입력한 숫자만큼 일자 구하는 함수
    @input int day 2
    @output String 20200622 (오늘은 20200620)
    * */
    public static String getSystemDate(int day){
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        cal.setTime(new Date());
        cal.add(Calendar.DATE, day);
        return df.format(cal.getTime());
    }


    /* 쿠폰번호 형식변경
    @input String couponNumber 111112222233333
    @Output String 11111-22222-33333
    * */
    public static String changeCouponStringStyle(String couponNumber){
        return couponNumber.substring(0,5)+"-"
                +couponNumber.substring(5,10)+"-"
                +couponNumber.substring(10,15);
    }

    /* 이름 마스킹 함수
    @input String userName 안락언
    @Output String 안*언
    * */
    public static String userNameMasking(String userName){

        if((userName != null) && (userName.length() > 1)){
            char[] userNameC = userName.toCharArray();
            userName.getChars(0, 1, userNameC, 0);
            Arrays.fill(userNameC, 1, userNameC.length-1, '*');
            return (new String(userNameC));
        }

        return userName;
    }


}
