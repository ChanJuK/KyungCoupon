package kyungCoupon.exception;

import kyungCoupon.util.OftenUsedFunction;

public class CouponAllUsedException extends RuntimeException {

    public CouponAllUsedException(){
        super("모든 쿠폰이 사용자가 정하지거나 사용되었습니다.");
    }

    public CouponAllUsedException(String couponNum){
        super("해당 쿠폰은 이미 사용완료되었습니다.[쿠론번호 : "
                + couponNum.substring(0,5)+"-"
                +couponNum.substring(5,10)+"-"
                +couponNum.substring(10,15)
        +"]");
    }
}
