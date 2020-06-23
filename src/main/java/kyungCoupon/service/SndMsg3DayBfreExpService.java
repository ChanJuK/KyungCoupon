package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.util.OftenUsedFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Service
public class SndMsg3DayBfreExpService {

    private CouponRepository couponRepository;

    @Autowired
    public SndMsg3DayBfreExpService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    /*
    7.발급된 쿠폰중 만료 3일전 사용자에게 메세지 발송하는 기능
    (실제 발송이 아닌 로그에 프린트로 출력함)
    @input String expAftDate N일 이후 만료, 예) expAftDate=3, 3일이후에 만려
    @output N건이 전송되었습니다.
    * */
    public int sndMsgToCustomers(String excDate) {

        int sndCnt = 0;

        //3일 이후 일자 구하기, 오늘+3일
        String date3Day = OftenUsedFunction.getAddedDate(excDate, 3);
        System.out.println("========>"+date3Day);


        //전송할 대상조회
        Iterator<Coupon> couponList = couponRepository.findByAndUserIdIsNotNullAndExpDateAndUseYN(date3Day, "N").iterator();

        //전송할 메시지
        String content = "@1 고객님, 쿠폰[@2]이 3일 후 만료됩니다.";
        String fromEmail = "admin@kyungcoupon.com";

        while(couponList.hasNext()){
            Coupon coupon = couponList.next();

            //메세지 전송
            System.out.println("******************* snd email *********************");
            System.out.println("To : "+coupon.getUser().getEmail());
            System.out.println(getContent(content, coupon));
            System.out.println("From : "+fromEmail);
            System.out.println("***************************************************");

            sndCnt ++;
        }

        return sndCnt;


    }

    //메시지 세팅
    public String getContent(String content, Coupon coupon){
        content = content.replace("@1", OftenUsedFunction.userNameMasking(coupon.getUser().getUserName()));
        content = content.replace("@2", coupon.getCouponNum().substring(0, 5));
        return content;
    }
}
