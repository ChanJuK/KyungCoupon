package kyungCoupon.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(){
        super("정상로그인되지 않았습니다.");
    }

}
