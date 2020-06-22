package kyungCoupon.exception;

public class AlreadyHasCouponException extends RuntimeException{

    public AlreadyHasCouponException(){
        super("이미 쿠폰을 지급 받았습니다.");
    }
}
