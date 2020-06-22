package kyungCoupon.service;

import io.jsonwebtoken.Claims;
import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.exception.AuthenticationException;
import kyungCoupon.exception.CouponAllUsedException;
import kyungCoupon.exception.CouponNotExistsException;
import kyungCoupon.util.OftenUsedFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class UseCouponService {
    private CouponRepository couponRepository;
    private UserRepository userRepository;

    @Autowired
    public UseCouponService(CouponRepository couponRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
    }


    /* 쿠폰을 사용처리하는 함수
     * @Param String couponNum
     * @return void
     * */
    public Coupon useCoupon(String couponNum, Long userId) {

        //쿠폰번호가 빈값인지 체크
        if(couponNum.isEmpty()){
            throw new CouponNotExistsException();
        }

        //가입한 회원인지 검증
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new AuthenticationException();
        }

        //쿠폰존재 여부 검증
        Coupon coupon = couponRepository.findByCouponNum(couponNum).orElse(null);
        if(coupon == null){
            throw new CouponNotExistsException(couponNum);
        }

        //유효기간 검증
        String todayStr = OftenUsedFunction.getTodayDate();
        int today = Integer.valueOf(todayStr);
        int expDate = Integer.valueOf(coupon.getExpDate());
        if(expDate <= today){
            throw new CouponNotExistsException(couponNum, coupon.getExpDate());
        }

        //쿠폰에 지정된 사용자 검증
        if(!userId.equals(coupon.getUser().getId())){
            throw new CouponNotExistsException(couponNum);
        }

        //기사용인지 검증
        if("Y".equals(coupon.getUseYN())){
            throw new CouponAllUsedException(couponNum);
        }

        //사용처리
        coupon.setUseYN("Y");
        coupon.setUseDate(todayStr);
        coupon.setCnclYN(null);
        coupon.setCnclDate(null);
        coupon.setUser(user);
        return couponRepository.save(coupon);

    }
}
