package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GenerateCouponService {


    private CouponRepository couponRepository;


    @Autowired
    public GenerateCouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    /*
     * 쿠폰 번호 정보 적재함수
     * @param BigDecimal couponCnt 발급할 쿠폰 개수
     * @param String validDate 유효기간 일자
     * @return 쿠폰 생성 건수
     */
    public int generateCouponNum(int couponCnt, String expDate) {

        Set<Coupon> couponHashSet = new HashSet<Coupon>();


        //중복제거하기위해 hashset사용
        while (couponHashSet.size() <= couponCnt){

            couponHashSet.add(Coupon.builder()
                    .couponNum(createNumber())//쿠폰번호 생성함수 호출
                    .expDate(expDate) //만료일자
                    .user(null) //고객 객체
                    .useYN("N") //사용여부
                    .build());

        }


        //한번에 저장
        couponRepository.saveAll(couponHashSet);

        return couponHashSet.size();


    }





    /*
     * 쿠폰 번호 생성함수
     * @param BigDecimal couponCnt
     * @return string
     */
    public String createNumber() {
        //15자리 랜덤 [0-9, a-z, A-Z] 자리수 구함.
        return RandomStringUtils.randomAlphanumeric(15);
    }


}

