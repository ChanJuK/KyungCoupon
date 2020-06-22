package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.domain.network.Enums;
import kyungCoupon.domain.network.Header;
import kyungCoupon.exception.AuthenticationException;
import kyungCoupon.exception.CouponNotExistsException;
import kyungCoupon.util.OftenUsedFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;



@Service
@Transactional
public class CnclUseCouponService {
    private CouponRepository couponRepository;
    private UserRepository userRepository;


    @Autowired
    public CnclUseCouponService(CouponRepository couponRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
    }


    /* 쿠폰을 사용처리 취소하는 함수
     * @Param String couponNum
     * @return void
     * */
    public Header<Coupon> cnclUseCoupon(String couponNum, Long userId) {

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

        //고객 id null체크
        if (coupon.getUser().getId() == null){
            throw new CouponNotExistsException(couponNum, false);
        }
        //쿠폰의 userId와 고객의 id가 동일한지 검증
        if(!userId.equals(coupon.getUser().getId())){
            throw new CouponNotExistsException(couponNum);
        }
        //쿠폰이 사용되지 않은건지 확인
        if("N".equals(coupon.getUseYN())){
            throw new CouponNotExistsException(false);
        }

        //사용처리
        coupon.setUseYN("N");
        coupon.setUseDate(null);
        coupon.setCnclYN("Y");
        coupon.setCnclDate(OftenUsedFunction.getTodayDate());
        coupon.setUser(user);
        try {
            couponRepository.save(coupon);
        }catch (Exception e){
            return Header.response(coupon, Enums.SYSTEM_ERROR.getId(), "취소처리 저장 중 에러발생했습니다." );
        }
        return Header.response(coupon, Enums.SUCCESS.getId(), Enums.SUCCESS.getDescription());
    }
}
