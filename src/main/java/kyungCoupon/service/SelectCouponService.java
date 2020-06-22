package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.exception.AuthenticationException;
import kyungCoupon.util.OftenUsedFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelectCouponService {
    private CouponRepository couponRepository;
    private UserRepository userRepository;

    @Autowired
    public SelectCouponService(CouponRepository couponRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
    }

    /*
     * 오늘 만료되는 쿠폰리스트 조회
     * @param void
     * @return List<Coupon>
     */
    public List<Coupon> getCouponExpiredToday() {

        return couponRepository.findByExpDate(OftenUsedFunction.getTodayDate());

    }


    /*
     * 해당고객에게 지급한 쿠폰을 조회
     * @param void
     * @return List<Coupon>
     */
    public List<Coupon> getMyCouponInfo(Long userId) {

        //기존에 가입한 회원인지 검증
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new AuthenticationException();
        }

        //쿠폰받은 조회
        List<Coupon> coupon = couponRepository.findByUserId(userId);

        return coupon;

    }
}
