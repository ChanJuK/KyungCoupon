package kyungCoupon.exception;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordInvalidException extends RuntimeException {
    public PasswordInvalidException(){
        super("비밀번호가 일치하지 않습니다.");
    }
}
