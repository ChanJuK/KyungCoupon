package kyungCoupon.service;

import kyungCoupon.domain.Coupon;
import kyungCoupon.domain.CouponRepository;
import kyungCoupon.domain.User;
import kyungCoupon.domain.UserRepository;
import kyungCoupon.domain.network.Enums;
import kyungCoupon.domain.network.Header;
import kyungCoupon.exception.*;
import kyungCoupon.util.OftenUsedFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class SetCouponUserService {

    private CouponRepository couponRepository;
    private UserRepository userRepository;

    @Autowired
    public SetCouponUserService(CouponRepository couponRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
    }


    public Header<String> setCouponUser(Long userId) {

        //기존에 가입한 회원인지 검증
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new AuthenticationException();
        }


        //이미 쿠폰받은 고객인지 검증
        List<Coupon> coupon = couponRepository.findByUserIdAndUseYNAndExpDateGreaterThanEqual(user.getId(),"N", OftenUsedFunction.getTodayDate());
        if(coupon.size() > 0){
            throw new AlreadyHasCouponException();
        }

        //사용자가 안정해진 쿠폰 한개만 조회
        Coupon oneCoupon = couponRepository.findFirstByUserIdIsNullAndExpDateGreaterThan(OftenUsedFunction.getTodayDate());
        if(oneCoupon == null){
            throw new CouponAllUsedException();
        }


        //쿠폰을 고객에게 할당
        oneCoupon.setUser(user);
        try {
            couponRepository.save(oneCoupon);
        }catch (Exception e){
            Header.response(Enums.SYSTEM_ERROR.getId(), Enums.SYSTEM_ERROR.getDescription());
        }
        return Header.response(OftenUsedFunction.changeCouponStringStyle(oneCoupon.getCouponNum()),
                Enums.SUCCESS.getId(), Enums.SUCCESS.getDescription());


    }
}
